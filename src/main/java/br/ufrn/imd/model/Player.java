package br.ufrn.imd.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private long id;
    private String username;
    private String password;
    private int rankPoints;
    private int eventPoints;
    private double winrate;
    private ArrayList<Long> opponentIds; // Store opponent IDs using ArrayList
    private double opponentsMatchWinrate;

    public Player(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rankPoints = 0;
        this.winrate = 0;
        this.eventPoints = 0;
        this.opponentIds = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public int getRankPoints() { return rankPoints; }
    public void setRankPoints(int rankPoints) { this.rankPoints = rankPoints; }
    
    public int getEventPoints() { return eventPoints; }
    public void setEventPoints(int eventPoints) { this.eventPoints = eventPoints; }
    
    public ArrayList<Long> getOpponentIds() { return opponentIds; } // Return ArrayList instead of List
    
    public double getWinrate() { return winrate; }

    public void setWinrate() {
        if (opponentIds.size() > 0) {
            this.winrate = (double) eventPoints / opponentIds.size();
        } else {
            this.winrate = 0.0;
        }
    }

    public double getOpponentsMatchWinrate() { return opponentsMatchWinrate; }
    
    public void setOpponentsMatchWinrate(double opponentsMatchWinrate) {
    	this.opponentsMatchWinrate = opponentsMatchWinrate;
    }

    public void addOpponentId(long opponentId) {
        opponentIds.add(opponentId);
    }

    public void removeOpponentId(long opponentId) {
        opponentIds.remove(opponentId);
    }
}
