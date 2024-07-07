package br.ufrn.imd.model;

import java.util.List;

public class TeamResult {
    private String teamId;
    private int eventPoints;
    private double winrate;
    private List<String> opponentTeamIds;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
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

    public List<String> getOpponentTeamIds() {
        return opponentTeamIds;
    }

    public void setOpponentTeamIds(List<String> opponentTeamIds) {
        this.opponentTeamIds = opponentTeamIds;
    }
}
