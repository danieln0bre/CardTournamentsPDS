package br.ufrn.imd.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//Especifica o collection "players2" que será usado pela classe Player.
@Document(collection = "players2")
public class Player extends User{
	
	// Usa a chave primária id da superclasse User.
	
    private int rankPoints;
    private int eventPoints;
    private double winrate;
    @DBRef
    private Deck deck;
    private ArrayList<String> appliedEventsId;
	private ArrayList<String> opponentIds;
    private double opponentsMatchWinrate;

    public Player(String name, String username,String email, String password) {
        super(name, username, email, password);
        this.rankPoints = 0;
        this.winrate = 0;
        this.eventPoints = 0;
        this.deck = new Deck();
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
    
    public String getDeckId() {
        return (this.deck != null) ? this.deck.getId() : null;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    public boolean hasDeck() {
        if(deck.getDeckName()!=null) {
        	return true;
        }else {
        	return false;
        }
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
