package br.ufrn.imd.service;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.repository.ManagerRepository;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
    
    public void removePlayer(Player player) {
    	playerRepository.delete(player);
    }
    
    public void removeAllPlayers() {
    	playerRepository.deleteAll();
    }

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }
    
    public void removeManager(Manager manager) {
    	managerRepository.delete(manager);
    }

    // Retorna um usuário identificado pelo ID. 
    // Se não encontra um usuário, retorna nulo.
    public User getUserById(String id, String userType) {
        if ("player".equals(userType)) {
            return playerRepository.findById(id).orElse(null);
        }
        if ("manager".equals(userType)) {
            return managerRepository.findById(id).orElse(null);
        }
        return null;
    }
}
