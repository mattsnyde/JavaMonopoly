import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.*;
public class SqlQueries {
    private static Connection connection;
     public static Connection createConnection() throws ClassNotFoundException{
        DBInfo db = new DBInfo();
        try{
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish a connection to SQL Server using Windows authentication and SSL encryption with trustServerCertificate=true
            String connectionUrl = "jdbc:sqlserver://" + db.getCompName() + ";databaseName=" + db.getDbName() + ";integratedSecurity=true;trustServerCertificate=true;";
            SqlQueries.connection = DriverManager.getConnection(connectionUrl);

            while(!connection.isValid(5000)){
                Thread.sleep(5000);
            }

            System.out.println("Connected in SqlQueries");

        }catch(SQLException e){
            e.printStackTrace();

            System.out.println("Not connected in SqlQueries");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public interface QueryCallback {
        void processResult(ResultSet resultSet) throws SQLException;
    }
    
    public static void executeQuery(Connection connection, String query, QueryCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(() -> {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                callback.processResult(resultSet);
            }
            return null;
        });
        executor.shutdown();
    }


 

    //Query to create game
        //Gathers ids of all existing games. If Id generated doesnt exist we prepare to execute INSERT query toherwise keep andomly generating IDs till it doesnt exist already
        //Set values of INSERT INTO need to specify 4 values [?, ?, ?, ?] = [gameid, gamename, gamepassword, gamebankbalance]
            //password is encrypted and then the bytes of that are stored. Balance is default to 15140 on a new game. 
            public static CompletableFuture<LocalStorage> createNewGame(String gamePassword, String gameName) {
                String existingGameQuery = "SELECT * FROM Game";
                Vector<Integer> gameId = new Vector<Integer>();
                CompletableFuture<LocalStorage> future = new CompletableFuture<>();
            
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(existingGameQuery);
                    ResultSet resultSet = preparedStatement.executeQuery();
            
                    while (resultSet.next()) {
                        int id = resultSet.getInt("gameid");
                        gameId.add(id);
                    }
            
                    Random rand = new Random();
                    int newGameId = rand.nextInt(2147483646) + 1;
            
                    while (gameId.contains(newGameId)) {
                        newGameId = rand.nextInt(2147483646) + 1;
                    }
            
                    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Game (gameid, gamename, gamepassword, gamebankbalance) VALUES(?, ?, ?, ?)");
                    insertStatement.setInt(1, newGameId);
                    insertStatement.setString(2, gameName);
            
                    EncryptDecrypt ed = new EncryptDecrypt();
                    String pass = ed.encrypt(gamePassword);
                    byte[] varBinaryValue = pass.getBytes(StandardCharsets.UTF_8);
                    insertStatement.setBytes(3, varBinaryValue);
                    insertStatement.setInt(4, 15140);
            
                    int rowsAffected = insertStatement.executeUpdate();
                    System.out.println("Num of rows affected: " + rowsAffected);
            
                    Game game = new Game(newGameId, gameName, 15140);
                    System.out.println("Creating: " + game.getGameBankBalance());
            
                    LocalStorage ls = new LocalStorage();
                    ls.setGame(game);
            
                    future.complete(ls);
            
                    return future;
                } catch (SQLException e) {
                    System.out.println("Error 1");
                    e.printStackTrace();
                    future.completeExceptionally(e);
                    return future.thenApply(result -> null);
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error 2");
                    e.printStackTrace();
                    future.completeExceptionally(e);
                    return future.thenApply(result -> null);
                }
            }
 

    //Query to retrieve game
    public static CompletableFuture<Game> getExistingGameAsync(String gamePassword, String gameName) {
        System.out.println("getExistingGameAsync");
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT g.gameid, g.gamename, g.gamebankbalance FROM Game AS g WHERE g.gamepassword = ? AND g.gamename = ?"
                );
                EncryptDecrypt ed = new EncryptDecrypt();
                String pass = ed.encrypt(gamePassword);
                byte[] varBinaryValue = pass.getBytes(StandardCharsets.UTF_8);
                preparedStatement.setBytes(1, varBinaryValue);
                preparedStatement.setString(2, gameName);
                System.out.println("inside of completable future");
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        int gameid = rs.getInt("gameid");
                        String gamename = rs.getString("gamename");
                        int gamebankbalance = rs.getInt("gamebankbalance");
                        Game game = new Game(gameid, gamename, gamebankbalance);
                        System.out.println("got game");
                        return game;
                    } else {
                        System.out.println("No results");
                        return null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

 
}
