package br.ufrn.imd.repository;

import br.ufrn.imd.model.Event;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
	// Apenas utiliza os métodos do MongoRepository.
}
