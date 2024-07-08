package br.ufrn.imd.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "players2")
public class Player extends User {

    private int rankPoints;
    private int eventPoints;
    private double winrate;
    private String gameObjectId;
    private List<String> appliedEventsId;
    private List<String> opponentIds;
    private List<Event> historicoEventos;
    private double opponentsMatchWinrate;
    private String teamId;

    public Player(String name, String username, String email, String password) {
        super(name, username, email, password, Role.ROLE_PLAYER);
        this.rankPoints = 0;
        this.eventPoints = 0;
        this.winrate = 0.0;
        this.gameObjectId = null;
        this.appliedEventsId = new ArrayList<>();
        this.opponentIds = new ArrayList<>();
        this.historicoEventos = new ArrayList<>();
        this.opponentsMatchWinrate = 0.0;
    }

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

    public String getGameObjectId() {
        return gameObjectId;
    }

    public void setGameObjectId(String gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public boolean hasGameObject() {
        return gameObjectId != null && !gameObjectId.trim().isEmpty();
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void clearOpponents() {
        opponentIds.clear();
    }

    public void clearAppliedEvents() {
        appliedEventsId.clear();
    }
}
