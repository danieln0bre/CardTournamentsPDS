package br.ufrn.imd.repository;

import br.ufrn.imd.model.PersistentLogin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersistentLoginRepository extends MongoRepository<PersistentLogin, String> {
    Optional<PersistentLogin> findBySeries(String series);
    void deleteByUsername(String username);
}
