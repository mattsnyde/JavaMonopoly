import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;


public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        SqlQueries.createConnection();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Menu driven program");
        System.out.println("1. Create new game");
        System.out.println("2. Enter existing game");

        int gameOption = scanner.nextInt();
        scanner.nextLine();
        String gameName = scanner.nextLine();
        String gamePassword = scanner.nextLine();
        System.out.println("The val of gameOption: " + gameOption);
        switch(gameOption){
            case 1:
                createNewGameCall(gamePassword, gameName);
                System.out.println("Case 1 end");
                break;
            case 2:
                
                // settingGame(existingGame);
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
    public static void createNewGameCall(String gamePassword, String gameName){
        SqlQueries.createNewGame(gamePassword, gameName)
        .thenApply(ls -> {
            System.out.println(ls.getGame().getGameBankBalance());
            // Do something else with the LocalStorage object
             return ls;
        })
        .thenAccept(ls -> {
            System.out.println("Result of operation: ");
            //Do something with the result like call another query using the information retrieved here 

        })
        .exceptionally(ex -> {
            System.out.println("An exception occurred: " + ex.getMessage());
            return null;
        });
    }
 
}
