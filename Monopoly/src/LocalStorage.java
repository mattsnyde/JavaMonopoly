public class LocalStorage {
    private int localGameId;
    private Game currentGame;
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
}
