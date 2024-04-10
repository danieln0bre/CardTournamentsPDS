package br.ufrn.imd.repository;

import java.util.ArrayList;
import br.ufrn.imd.model.Player;

public class EventRankingRepository {
	private ArrayList<Player> playersEventRanking;

	public EventRankingRepository() {
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
