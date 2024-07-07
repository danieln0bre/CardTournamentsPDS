package br.ufrn.imd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "times")
public class Team {
    @Id
    private String id;
    private String name;
    private String ownerId;
    private List<String> eventIds;
    private List<String> playerIds;
    private int eventPoints;

    public Team(String name, String ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.playerIds = new ArrayList<>(5);
        this.eventIds = new ArrayList<>();
    }

    public boolean addPlayer(Player player) {
        if (playerIds.size() < 5) {
            player.setTeamId(this.id);
            return playerIds.add(player.getId());
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        player.setTeamId(null);
        return playerIds.remove(player.getId());
    }

    public List<String> getPlayerIds() {
        return new ArrayList<>(playerIds);
    }

    public List<String> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<String> eventIds) {
        this.eventIds = eventIds;
    }

    public void addEventId(String eventId) {
        if (!this.eventIds.contains(eventId)) {
            this.eventIds.add(eventId);
        } else {
            throw new IllegalArgumentException("Event ID already added.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEventPoints() {
        return eventPoints;
    }

    public void setEventPoints(int eventPoints) {
        this.eventPoints = eventPoints;
    }
}
