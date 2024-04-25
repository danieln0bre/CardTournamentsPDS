package br.ufrn.imd.service;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.repository.ManagerRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.util.PlayerValidationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public Player savePlayer(Player player) {
        PlayerValidationUtil.validatePlayer(player);
        return playerRepository.save(player);
    }
    
    public void removePlayer(Player player) {
        if (player == null || player.getId() == null) {
            throw new IllegalArgumentException("Player or player ID cannot be null for deletion.");
        }
        playerRepository.delete(player);
    }
    
    public void removeAllPlayers() {
        playerRepository.deleteAll();
    }

    public Manager saveManager(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null.");
        }
        // Assuming similar validation needs for Manager, though not implemented here
        return managerRepository.save(manager);
    }
    
    public void removeManager(Manager manager) {
        if (manager == null || manager.getId() == null) {
            throw new IllegalArgumentException("Manager or manager ID cannot be null for deletion.");
        }
        managerRepository.delete(manager);
    }

    // Retrieves a user by ID based on user type.
    public User getUserById(String id, String userType) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (!"player".equals(userType) && !"manager".equals(userType)) {
            throw new IllegalArgumentException("Invalid or null user type. Valid options are 'player' or 'manager'.");
        }

        if ("player".equals(userType)) {
            return playerRepository.findById(id).orElse(null);
        }
        if ("manager".equals(userType)) {
            return managerRepository.findById(id).orElse(null);
        }
        return null;
    }
}
