import java.util.Random;
import java.util.Vector;

public class DiceRoller {
    private static int die1;
    private static int die2;
    private static Random rand = new Random();

    public static void setDie(){
        DiceRoller.die1 = rand.nextInt(6) + 1;
        DiceRoller.die2 = rand.nextInt(6) + 1;
    }

    public static Vector<Integer> getDieValues(){
        Vector<Integer> dieValues = new Vector<Integer>();
        dieValues.add(die1);
        dieValues.add(die2);
        return dieValues;
    }
}
