package br.ufrn.imd.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players2")
public class Player extends User{
    private int rankPoints;
    private int eventPoints;
    private double winrate;
    private String deck;
    private ArrayList<String> appliedEventsId;
	private ArrayList<String> opponentIds;
    private double opponentsMatchWinrate;

    public Player(String name, String username, String password) {
        super(name, username, password);
        this.rankPoints = 0;
        this.winrate = 0;
        this.eventPoints = 0;
        this.deck = null;
        this.appliedEventsId = new ArrayList<String>();
        this.opponentIds = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
    }

    // Métodos auxiliares.
    
    public void addEventPoints(int point) {
    	this.eventPoints += point;
    }
    
    public void addEventId(String eventId) {
    	appliedEventsId.add(eventId);
    }
    
    public void addOpponentId(String opponentId) {
        opponentIds.add(opponentId);
    }

    public void removeOpponentId(String opponentId) {
        opponentIds.remove(opponentId);
    }
    
    // Gets e sets.
    
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
    
    public double getWinrate() {
    	return winrate;
    }

    public void setWinrate(double winrate) {
    	this.winrate = winrate;
    }
    
    public String getDeck() {
		return deck;
	}

	public void setDeck(String deck) {
		this.deck = deck;
	}
    
	public ArrayList<String> getAppliedEventsId() {
        return appliedEventsId;
    }
    
    public ArrayList<String> getOpponentIds() {
    	return opponentIds;
    }

    public double getOpponentsMatchWinrate() {
    	return opponentsMatchWinrate;
    }

    public void setOpponentsMatchWinrate(double opponentsMatchWinrate) {
        this.opponentsMatchWinrate = opponentsMatchWinrate;
    }
}
