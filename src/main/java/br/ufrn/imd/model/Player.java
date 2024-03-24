package br.ufrn.imd.model;

public class Player extends User{

    private Deck deck;
    private String id;
    
    public Player(String username, String password) {
		super(username, password);
	}
    // other attributes and methods
}