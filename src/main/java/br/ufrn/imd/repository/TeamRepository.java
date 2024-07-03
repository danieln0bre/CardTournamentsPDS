package br.ufrn.imd.repository;

import br.ufrn.imd.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}