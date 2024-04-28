package br.ufrn.imd.repository;
import br.ufrn.imd.model.Deck;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeckRepository extends MongoRepository<Deck, String>{
	boolean existsById(String id);
	Optional<Deck> findById(String id);
}
