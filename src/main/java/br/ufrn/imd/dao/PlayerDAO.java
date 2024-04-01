package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Player;

public class PlayerDAO {

	private ArrayList<Player> players;

	public PlayerDAO() {
		this.players = new ArrayList<>();
	}
	
	public void adicionarPlayer(Player player) {
		players.add(player);
	}
	
	public void removerPlayer(Player player) {
		players.remove(player);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
