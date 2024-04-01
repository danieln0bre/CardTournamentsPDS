package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Player;

public class GeneralRankingDAO {
	
	private ArrayList<Player> playersGeneralRanking;

	public GeneralRankingDAO() {
		this.playersGeneralRanking = new ArrayList<>();
	}
	
	public void adicionarPlayer(Player player) {
		playersGeneralRanking.add(player);
	}
	
	public void removerPlayer(Player player) {
		playersGeneralRanking.remove(player);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
