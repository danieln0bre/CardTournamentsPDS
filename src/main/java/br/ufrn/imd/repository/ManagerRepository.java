package br.ufrn.imd.repository;

import br.ufrn.imd.model.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ManagerRepository extends MongoRepository<Manager, String> {
    
    // Encontra Managers por nome.
    List<Manager> findByName(String name);
    
    // Encontra Managers cujo username comece com um prefixo específico.
    List<Manager> findByUsernameStartingWith(String prefix);
}
