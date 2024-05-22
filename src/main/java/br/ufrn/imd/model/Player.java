package br.ufrn.imd.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a player with rankings, events, and performance statistics.
 */
@Document(collection = "players2")
public class Player extends User {

    private int rankPoints;
    private int eventPoints;
    private double winrate;
    private Deck deck;
    private List<String> appliedEventsId;
    private List<String> opponentIds;
    private List<Event> historicoEventos;
    private double opponentsMatchWinrate;

    public Player(String name, String username, String email, String password) {
        super(name, username, email, password, Role.ROLE_PLAYER);
        this.rankPoints = 0;
        this.eventPoints = 0;
        this.winrate = 0.0;
        this.deck = new Deck();
        this.appliedEventsId = new ArrayList<>();
        this.opponentIds = new ArrayList<>();
        this.historicoEventos = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
    }

    // Auxiliary methods to manipulate player data.

    public void addEventPoints(int points) {
        this.eventPoints += points;
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

    // Getters and setters.

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

    public Deck getDeck() {
        return deck;
    }
    
    public String getDeckId() {
    	return deck.getId();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public boolean hasDeck() {
        return deck != null && deck.getDeckName() != null;
    }

    public List<String> getAppliedEventsId() {
        return new ArrayList<>(appliedEventsId);
    }

    public List<String> getOpponentIds() {
        return new ArrayList<>(opponentIds);
    }

    public List<Event> getHistoricoEventos() {
        return new ArrayList<>(historicoEventos);
    }

    public void addHistoricoEvento(Event event) {
        historicoEventos.add(event);
    }

    public double getOpponentsMatchWinrate() {
        return opponentsMatchWinrate;
    }

    public void setOpponentsMatchWinrate(double opponentsMatchWinrate) {
        this.opponentsMatchWinrate = opponentsMatchWinrate;
    }

    // Clear methods for lists

    public void clearOpponents() {
        opponentIds.clear();
    }

    public void clearAppliedEvents() {
        appliedEventsId.clear();
    }
}