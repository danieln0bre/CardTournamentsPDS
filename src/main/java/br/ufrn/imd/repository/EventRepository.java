package br.ufrn.imd.repository;

import br.ufrn.imd.model.Event;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EventRepository extends MongoRepository<Event, String> {
	// Apenas utiliza os métodos do MongoRepository.
}
