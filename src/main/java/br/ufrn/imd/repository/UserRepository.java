package br.ufrn.imd.repository;

import br.ufrn.imd.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
