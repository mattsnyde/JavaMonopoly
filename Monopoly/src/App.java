import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class App {
    public static void main(String[] args) {
        DBInfo db = new DBInfo();
        EncryptDecrypt et = new EncryptDecrypt();
        String in = "test";
        String hashed = "";
        try {
            hashed = et.encrypt(in);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Hashed: " + hashed);
        Boolean decrypted = false;
        try {
            decrypted = et.decrypt(in,hashed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Decrypted: " + decrypted);
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish a connection to SQL Server using Windows authentication and SSL encryption with trustServerCertificate=true
            String connectionUrl = "jdbc:sqlserver://" + db.getCompName() + ";databaseName=" + db.getDbName() + ";integratedSecurity=true;trustServerCertificate=true;";
            Connection connection = DriverManager.getConnection(connectionUrl);

            // Use the connection object here
            // ...

            String query = "Select * FROM ActionCard";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()){
                    int id = rs.getInt("actionid");
                    System.out.println(id);
                }
            }catch(SQLException i){
                System.out.println("SQL Exception: " + i.getMessage());
            }

            System.out.println("Successfully connected");
            connection.close();
        } catch (ClassNotFoundException j) {
            System.out.println("Could not load SQL Server JDBC driver: " + j.getMessage());
        } catch (SQLException h) {
            System.out.println("SQL Exception: " + h.getMessage());
        }
     }
}
