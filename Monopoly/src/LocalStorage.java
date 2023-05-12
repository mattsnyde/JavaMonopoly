import java.util.Vector;

public class LocalStorage {
    private int localGameId;
    private Game currentGame;
    private Vector<Player> localPlayer = new Vector<Player>();
    public void setLocalGameId(int localGameId){
        this.localGameId = localGameId;
    }
    public int getLocalGameId(){
        return localGameId;
    }

    public void setGame(Game currentGame){
        this.currentGame = currentGame;
    }

    public Game getGame(){
        return currentGame;
    }

    public void setPlayer(Player player){
        this.localPlayer.add(player);
    }

    public Vector<Player> getPlayers(){
        return localPlayer;
    }
}
