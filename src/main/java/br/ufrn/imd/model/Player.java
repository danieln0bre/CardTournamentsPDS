package br.ufrn.imd.model;

public class Player extends User{

    private Deck deck;
    private long id;
    private int points;
    
    public Player(long id, String username, String password) {
		super(username, password);
		this.id = id;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
    
    
}