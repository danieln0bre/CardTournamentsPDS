package br.ufrn.imd.repository;

//import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

//import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;


public interface EventRankingRepository extends MongoRepository<Player, String> {
	/*private ArrayList<Player> playersEventRanking;

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
		
	}*/
}
