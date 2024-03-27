package br.ufrn.imd.model;

public class Pairing {
    private Player player1;
    private Player player2;
    private int result;
    
    public Pairing(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = -1;
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    
    public int getResult() {
        return result;
    }
    
    public void setResult(int result) {
        this.result = result;
    }
}
