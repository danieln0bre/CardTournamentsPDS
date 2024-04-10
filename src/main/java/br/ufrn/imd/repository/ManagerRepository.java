package br.ufrn.imd.repository;

import br.ufrn.imd.model.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ManagerRepository extends MongoRepository<Manager, String> {
    
    // Encontrar Managers por nome
    List<Manager> findByName(String name);
    
    // Encontrar Managers cujo username comece com um prefixo específico
    List<Manager> findByUsernameStartingWith(String prefix);
    
    // Buscar Managers que gerenciam mais de um certo número de eventos
    @Query("{'eventos.1': {$exists: true}}")
    List<Manager> findByManagingMoreThanOneEvent();
    
    // Buscar Managers por um critério de nome de evento específico
    @Query("{'eventos.name': ?0}")
    List<Manager> findByEventName(String eventName);
}
