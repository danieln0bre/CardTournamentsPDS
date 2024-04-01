package br.ufrn.imd.model;

import java.util.ArrayList;
import java.util.List;

public class Player extends User{
	
	private long id;
    private int rankPoints;
    private int eventPoints;
    private List<Player> opponents;
    private double opponentsMatchWinrate;
    private double opponentsOpponentsMatchWinrate;

    public Player(long id, String username, String password) {
        super(username, password);
        this.rankPoints = 0;
        this.eventPoints = 0;
        this.opponents = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
        this.opponentsOpponentsMatchWinrate = 0.0;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }
    
    public int getRankPoints() { return this.rankPoints; }
    public void setRankPoints(int rankPoints) { this.rankPoints = rankPoints; }
    
    public int getEventPoints() { return this.eventPoints; }
    public void setEventPoints(int eventPoints) { this.eventPoints = eventPoints; }
    
    public List<Player> getOpponents() { return opponents; }

    public double getOpponentsMatchWinrate() { return opponentsMatchWinrate; }

    public double getOpponentsOpponentsMatchWinrate() { return opponentsOpponentsMatchWinrate; }

    // Methods
    public void addOpponent(Player opponent) { opponents.add(opponent); }

    public void calculateOpponentsMatchWinrate() {
        if (!opponents.isEmpty()) {
            int totalWins = 0;
            int totalOpponentsMatches = 0;
            for (Player opponent : opponents) {
                totalWins += opponent.getEventPoints() / 3;
                totalOpponentsMatches += opponent.getOpponents().size();
            }
            opponentsMatchWinrate = totalWins / (double) totalOpponentsMatches;
        }
    }

    public void calculateOpponentsOpponentsMatchWinrate() {
        if (!opponents.isEmpty()) {
            double totalOpponentsOpponentsMatchWinrate = 0.0;
            int totalOpponents = 0;
            for (Player opponent : opponents) {
                for (Player opponentsOpponent : opponent.getOpponents()) {
                    totalOpponentsOpponentsMatchWinrate += opponentsOpponent.getOpponentsMatchWinrate();
                    totalOpponents++;
                }
            }
            if (totalOpponents > 0) {
                opponentsOpponentsMatchWinrate = totalOpponentsOpponentsMatchWinrate / totalOpponents;
            }
        }
    }

}
