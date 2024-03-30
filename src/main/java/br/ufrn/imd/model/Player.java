// Player.java
package br.ufrn.imd.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private long id;
    private String username;
    private String password;
    private int rankPoints;
    private int eventPoints;
    private List<Player> opponents;
    private double opponentsMatchWinrate;
    private double opponentsOpponentsMatchWinrate;

    public Player(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rankPoints = 0; // Default rankPoints
        this.eventPoints = 0; // Default eventPoints
        this.opponents = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
        this.opponentsOpponentsMatchWinrate = 0.0;
    }

    public int getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(int rankPoints) {
        this.rankPoints = rankPoints;
    }

    public int getEventPoints() {
        return eventPoints;
    }

    public void setEventPoints(int eventPoints) {
        this.eventPoints = eventPoints;
    }

    public List<Player> getOpponents() {
        return opponents;
    }

    public void addOpponent(Player opponent) {
        opponents.add(opponent);
    }

    public double getOpponentsMatchWinrate() {
        return opponentsMatchWinrate;
    }

    public double getOpponentsOpponentsMatchWinrate() {
        return opponentsOpponentsMatchWinrate;
    }

    public void calculateOpponentsMatchWinrate() {
        if (!opponents.isEmpty()) {
            int totalOpponentsMatches = 0;
            int totalWins = 0;
            for (Player opponent : opponents) {
                int winsAgainstOpponent = 0;
                for (Player o : opponent.opponents) {
                    if (o == this) {
                        winsAgainstOpponent++;
                    }
                }
                totalWins += winsAgainstOpponent;
                totalOpponentsMatches += opponent.opponents.size();
            }
            opponentsMatchWinrate = (double) totalWins / totalOpponentsMatches;
        }
    }

    public void calculateOpponentsOpponentsMatchWinrate() {
        if (!opponents.isEmpty()) {
            double totalOpponentsOpponentsWinrate = 0.0;
            for (Player opponent : opponents) {
                totalOpponentsOpponentsWinrate += opponent.opponentsMatchWinrate;
            }
            opponentsOpponentsMatchWinrate = totalOpponentsOpponentsWinrate / opponents.size();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
