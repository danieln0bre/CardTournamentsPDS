package br.ufrn.imd.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.ufrn.imd.model.Player;

public interface GeneralRankingRepository extends MongoRepository<Player, String> {
	// Apenas utiliza os métodos do MongoRepository.
}
