package br.ufrn.imd.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "event")
public class Event {

    private String name;
    private String date;
    private String location;
    private int numberOfRounds;
    @Id
    private String id;
    private int currentRound;
    private boolean finished;
    private List<Player> players;  // Atributo para armazenar os jogadores participantes do evento
    private List<Pairing> pairings;
    
    // Construtor inicializa players como uma lista vazia
    public Event(String name, String date, String location, int numberOfRounds) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.numberOfRounds = numberOfRounds;
        this.players = new ArrayList<Player>();  // Inicializa players como uma lista vazia
        this.currentRound = 0;
        this.finished = false;
        this.setPairings(new ArrayList<Pairing>());
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
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

	public List<Pairing> getPairings() {
		return pairings;
	}

	public void setPairings(List<Pairing> pairings) {
		this.pairings = pairings;
	}
    
}
