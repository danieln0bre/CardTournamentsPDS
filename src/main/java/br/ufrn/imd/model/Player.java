package br.ufrn.imd.model;

public class Player extends User{

    private Deck deck;
    private long id;
    private int eventPoints;
    private int rankPoints;
    
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
    
    
}