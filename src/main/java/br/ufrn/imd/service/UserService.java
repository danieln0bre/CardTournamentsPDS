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

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public User getUserById(String id, String userType) {
        if ("player".equals(userType)) {
            return playerRepository.findById(id).orElse(null);
        } else if ("manager".equals(userType)) {
            return managerRepository.findById(id).orElse(null);
        }
        return null;  // or throw an exception if preferred
    }
}
