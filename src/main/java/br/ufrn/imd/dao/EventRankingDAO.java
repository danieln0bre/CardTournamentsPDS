package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Player;

public class EventRankingDAO {
	private ArrayList<Player> playersEventRanking;

	public EventRankingDAO() {
		this.playersEventRanking = new ArrayList<>();
	}
	
	public void adicionarPlayer(Player player) {
		playersEventRanking.add(player);
	}
	
	public void removerPlayer(Player player) {
		playersEventRanking.remove(player);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
