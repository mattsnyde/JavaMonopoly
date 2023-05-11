public class Game {
    private int gameid;
    private String gamename;
    private int gamebankbalance;
    Game(int gameid, String gamename, int gamebankbalance){
        this.gameid = gameid;
        this.gamename = gamename;
        this.gamebankbalance = gamebankbalance;
    }
    public void setGameId(int gameid){
        this.gameid = gameid;
    }

    public void setGameName(String gamename){
        this.gamename = gamename;
    }

    public void setGameBankBalance(int gamebankbalance){
        this.gamebankbalance = gamebankbalance;
    }

    public int getGameId(){
        return gameid;
    }

    public String getGameName(){
        return gamename;
    }

    public int getGameBankBalance(){
        return gamebankbalance;
    }
}
