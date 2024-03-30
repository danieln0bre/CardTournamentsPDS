package br.ufrn.imd.model;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private String name;
    private Date date;
    private String location;
    private int numberOfRounds;
    private long id;
    private ArrayList<Pairing> pairings;
    private int currentRound;
    private boolean finished;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
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
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public ArrayList<Pairing> getPairings() {
        return pairings;
    }
    
    public void setPairings(ArrayList<Pairing> pairings) {
        this.pairings = pairings;
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
    
    // Constructor
    public Event(String name, Date date, String location, int numberOfRounds) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.numberOfRounds = numberOfRounds;
        this.currentRound = 1;
        this.finished = false;
    }
    
    public void start() {
    }
    

    public void update(String name, Date date, String location, int numberOfRounds) {
    	this.setName(name);
    	this.setDate(date);
    	this.setLocation(location);
    	this.setNumberOfRounds(numberOfRounds);
    }
    
}
