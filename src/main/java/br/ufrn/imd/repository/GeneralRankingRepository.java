package br.ufrn.imd.repository;

//import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

//import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;

public interface GeneralRankingRepository extends MongoRepository<Player, String> {
	
	/*private ArrayList<Player> playersGeneralRanking;

	public GeneralRankingRepository() {
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
		
	}*/
}
