package br.ufrn.imd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Document(collection = "event")
public class Event {
    @Id
    private String id;
    private String name;
    private String date;
    private String location;
    private int numberOfRounds;
    private int currentRound;
    private boolean hasStarted;
    private boolean finished;
    private List<String> entityIds;
    private List<Pairing> pairings;
    private boolean isTeamEvent;
    private String managerId;

    public Event(String name, String date, String location, int numberOfRounds, String managerId) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.numberOfRounds = numberOfRounds;
        this.currentRound = 0;
        this.finished = false;
        this.hasStarted = false;
        this.entityIds = new ArrayList<>();
        this.pairings = new ArrayList<>();
        this.managerId = managerId;
    }

    public void updateDetailsFrom(Event source) {
        this.name = source.name;
        this.date = source.date;
        this.location = source.location;
        this.numberOfRounds = source.numberOfRounds;
        this.currentRound = source.currentRound;
        this.finished = source.finished;
        this.hasStarted = source.hasStarted;
        setEntityIds(new ArrayList<>(source.entityIds));
        setPairings(new ArrayList<>(source.pairings));
    }

    public void addEntityId(String entityId) {
        if (!entityIds.contains(entityId)) {
            entityIds.add(entityId);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public List<String> getEntityIds() {
        return Collections.unmodifiableList(entityIds);
    }

    public void setEntityIds(List<String> entityIds) {
        this.entityIds = new ArrayList<>(entityIds);
    }

    public List<Pairing> getPairings() {
        return Collections.unmodifiableList(pairings);
    }

    public void setPairings(List<Pairing> pairings) {
        this.pairings = new ArrayList<>(pairings);
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

	public boolean isTeamEvent() {
		return isTeamEvent;
	}

	public void setTeamEvent(boolean isTeamEvent) {
		this.isTeamEvent = isTeamEvent;
	}
}
