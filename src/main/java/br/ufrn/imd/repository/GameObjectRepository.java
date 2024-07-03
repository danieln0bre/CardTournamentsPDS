package br.ufrn.imd.repository;
import br.ufrn.imd.model.GameObject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GameObjectRepository extends MongoRepository<GameObject, String>{
	boolean existsById(String id);
	Optional<GameObject> findById(String id);
    List<GameObject> findAll();
}
