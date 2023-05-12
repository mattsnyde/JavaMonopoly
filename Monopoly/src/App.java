import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

//Possile Player icons
enum icons{SCOTTIE_DOG, TOP_HAT, CAR, BATTLE_SHIP, THIMBLE, BOOT, WHEELBARROW, CAT};

public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        SqlQueries.createConnection(); //Establish connection to MonopolyStore DB hosted local
        Scanner scanner = new Scanner(System.in);

        //Menu
        System.out.println("Menu driven program");
        System.out.println("1. Create new game");
        System.out.println("2. Enter existing game");

        //Get game information, used to either create or login 
        int gameOption = scanner.nextInt();
        scanner.nextLine();
        String gameName = scanner.nextLine();
        String gamePassword = scanner.nextLine();
        System.out.println("Gamename: " + gameName + " gamePassword: " + gamePassword);
        System.out.println("The val of gameOption: " + gameOption);
        switch(gameOption){
            case 1: //Create a new game
                createNewGameCall(gamePassword, gameName);

                System.out.println("Case 1 end");
                break;
            case 2:
                getExistingGameCall(gamePassword, gameName); //login to existing game 
                System.out.println("Case 2 end");
                break;
            default:
                System.out.println("Potential problem with input");
        }
        scanner.close();
        // DiceRoller.setDie();
        // Vector<Integer> dieTest = DiceRoller.getDieValues();
        // for(int elem: dieTest){
        //     System.out.println(elem);
        // }
    }

    
    //Need to convert the enum optioin for player icon to string
    public static String playerIconSelection(int option){
        switch(option){
            case 0:
                return "SCOTTIE_DOG";
            case 1:
                return "TOP_HAT";
            case 2:
                return "CAR";
            case 3:
                return "BATTLESHIP";
            case 4:
                return "THIMBLE";
            case 5:
                return "BOOT";
            case 6:
                return "WHEELBARROW";
            case 7:
                return "CAT";
        }
        return null;
    }

    //Creating a new game, with password and a name
    public static void createNewGameCall(String gamePassword, String gameName){
        SqlQueries.createNewGame(gamePassword, gameName)
        .thenApply(ls -> {
             return ls; //ls is LocalStoage, need to return 
        })
        .thenAccept(ls -> {
            //Upon accepting ls then we can begin creating new players.
                //Player created through getting the gameId, then prompt the user for a playername/playerIcon, then call addNewPlayers function and store players in ls
            System.out.println("Result of operation: " + ls.getGame().getGameBankBalance());
            Scanner scanner = new Scanner(System.in);
            String exit = "E";
            Vector<Player> players = new Vector<Player>();
            int generalGameId = ls.getGame().getGameId();
            while(exit.compareTo("Q") != 0){
                String playerName = scanner.nextLine();
                System.out.println("PlayerName: " + playerName);
                int tempIcon = scanner.nextInt();
                String playerIcon = playerIconSelection(tempIcon);
                System.out.println("PlayerIcon: " + playerIcon);
                scanner.nextLine();
                SqlQueries.addNewPlayers(generalGameId, playerName, playerIcon)
                .thenApply(player -> {
                    players.add(player);
                    ls.setPlayer(player);
                    return player;
                })
                .thenAccept(player->{
                    for(Player auto: ls.getPlayers()){
                        System.out.println("The player id: " + auto.getPlayerId());
                    }

                })
                .exceptionally(ex -> {
                    return null;
                });
                exit = scanner.nextLine();
            }
            scanner.close();
        })
        .exceptionally(ex -> {
            System.out.println("An exception occurred: " + ex.getMessage());
            return null;
        });
    }

    public static void getExistingGameCall(String gamePassword, String gameName){
        SqlQueries.getExistingGameAsync(gamePassword, gameName)
        .thenApply(ls -> {
            System.out.println(ls.getGame().getGameBankBalance());
            // Do something else with the LocalStorage object
             return ls;
        })
        .thenAccept(ls -> {
            System.out.println("Result of operation: ");
            //Do something with the result like call another query using the information retrieved here 
            int gameId = ls.getGame().getGameId();
            System.out.println("Gameid: " + gameId);
            SqlQueries.getPreviousPlayers(gameId)
            .thenApply(players ->{
                for(Player auto: players){
                    ls.setPlayer(auto);
                }
                return players;
            })
            .thenAccept(players->{
                for(Player auto: ls.getPlayers()){
                    System.out.println("Player: " + auto.getPlayerId() + " " + auto.getGameId());
                }
            })
            .exceptionally(ex -> {
                System.out.println("An exception occurred: " + ex.getMessage());
                return null;
            });

        })
        .exceptionally(ex -> {
            System.out.println("An exception occurred: " + ex.getMessage());
            return null;
        });
    }
 
}
