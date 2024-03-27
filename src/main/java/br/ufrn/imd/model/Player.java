package br.ufrn.imd.model;

public class Player extends User{

    private Deck deck;
    private long id;
    
    public Player(long id, String username, String password) {
		super(username, password);
		this.id = id;
	}
}