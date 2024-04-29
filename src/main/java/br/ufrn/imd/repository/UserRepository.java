package br.ufrn.imd.repository;

import br.ufrn.imd.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends MongoRepository<User, String> {
	// Apenas utiliza os métodos do MongoRepository.
}
