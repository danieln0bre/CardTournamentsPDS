package br.ufrn.imd.model;

public class Pairing {
    private String playerOneId;
    private String playerTwoId;
    private int result;

    public Pairing(String playerOneId, String playerTwoId) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.result = -1;  // -1: ainda não existe um resultado.
    }

    public String getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(String playerOneId) {
        this.playerOneId = playerOneId;
    }

    public String getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
