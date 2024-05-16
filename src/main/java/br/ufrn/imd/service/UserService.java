package br.ufrn.imd.service;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.repository.ManagerRepository;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PlayerRepository playerRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public UserService(PlayerRepository playerRepository, ManagerRepository managerRepository) {
        this.playerRepository = playerRepository;
        this.managerRepository = managerRepository;
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<Player> player = playerRepository.findByUsername(username);
        if (player.isPresent()) {
            return Optional.of(player.get());
        }

        Optional<Manager> manager = managerRepository.findByUsername(username);
        if (manager.isPresent()) {
            return Optional.of(manager.get());
        }

        return Optional.empty();
    }

    public boolean checkPassword(User user, String rawPassword) {
        // Compare the raw password directly with the stored password
        return rawPassword.equals(user.getPassword());
    }

    // Save Player
    public Player savePlayer(Player player) {
        // Directly save the raw password
        return playerRepository.save(player);
    }

    // Save Manager
    public Manager saveManager(Manager manager) {
        // Directly save the raw password
        return managerRepository.save(manager);
    }

    // Get user by ID and type
    public Optional<User> getUserById(String id, String userType) {
        if ("player".equalsIgnoreCase(userType)) {
            return playerRepository.findById(id).map(player -> (User) player);
        } else if ("manager".equalsIgnoreCase(userType)) {
            return managerRepository.findById(id).map(manager -> (User) manager);
        }
        return Optional.empty();
    }
}
