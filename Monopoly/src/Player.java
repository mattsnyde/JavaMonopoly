public class Player {
    private int playerid;
    private int gameid;
    private String playername;
    private String playericon;
    private int balance;

    Player(int playerid, int gameid, String playername, String playericon, int balance){
        this.playerid = playerid;
        this.gameid = gameid;
        this.playername = playername;
        this.playericon = playericon;
        this.balance = balance;
    }

    public void setPlayerId(int playerid){
        this.playerid = playerid;
    }

    public void setGameId(int gameid){
        this.gameid = gameid;
    }

    public void setPlayerName(String playername){
        this.playername  = playername;
    }

    public void setPlayerIcon(String playericon){
        this.playericon = playericon;
    }

    public void setBalance(int balance){
        this.balance = balance;
    }

    public int getPlayerId(){
        return playerid;
    }

    public int getGameId(){
        return gameid;
    }

    public String getPlayerName(){
        return playername;
    }

    public String getPlayerIcon(){
        return playericon;
    }

    public int getBalance(){
        return balance;
    }
}
