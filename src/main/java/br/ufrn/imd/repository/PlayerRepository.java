package br.ufrn.imd.repository;

import br.ufrn.imd.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findById(String id);
    List<Player> findTop10ByOrderByIdAsc();
    List<Player> findAllById(Iterable<String> ids);
}
