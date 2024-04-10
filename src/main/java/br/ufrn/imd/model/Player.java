package br.ufrn.imd.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players2")
public class Player extends User{
    private int rankPoints;
    private int eventPoints;
    private double winrate;
    private ArrayList<String> opponentIds; // Store opponent IDs using ArrayList
    private double opponentsMatchWinrate;

    public Player(String name, String username, String password) {
        super(name, username, password);
        this.rankPoints = 0;
        this.winrate = 0;
        this.eventPoints = 0;
        this.opponentIds = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
    }

    // Other getters and setters
    public int getRankPoints() { return rankPoints; }
    public void setRankPoints(int rankPoints) { this.rankPoints = rankPoints; }

    public int getEventPoints() { return eventPoints; }
    public void setEventPoints(int eventPoints) { this.eventPoints = eventPoints; }

    public ArrayList<String> getOpponentIds() { return opponentIds; }

    public double getWinrate() { return winrate; }

    public void setWinrate(double winrate) {
    	this.winrate = winrate;
    }

    public double getOpponentsMatchWinrate() { return opponentsMatchWinrate; }

    public void setOpponentsMatchWinrate(double opponentsMatchWinrate) {
        this.opponentsMatchWinrate = opponentsMatchWinrate;
    }

    public void addOpponentId(String opponentId) {
        opponentIds.add(opponentId);
    }

    public void removeOpponentId(String opponentId) {
        opponentIds.remove(opponentId);
    }
}
