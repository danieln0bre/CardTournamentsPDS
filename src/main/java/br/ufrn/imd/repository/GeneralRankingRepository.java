package br.ufrn.imd.repository;

//import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

//import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;

public interface GeneralRankingRepository extends MongoRepository<Player, String> {
}
