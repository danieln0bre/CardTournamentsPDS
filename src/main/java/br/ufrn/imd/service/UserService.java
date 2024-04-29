package br.ufrn.imd.service;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.repository.ManagerRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.util.PlayerValidationUtil;

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

    public Player savePlayer(Player player) {
        PlayerValidationUtil.validatePlayer(player);
        return playerRepository.save(player);
    }

    public Manager saveManager(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null.");
        }
        return managerRepository.save(manager);
    }

    // Retrieves a user by ID based on user type and returns an Optional<User>
    public Optional<User> getUserById(String id, String userType) {
        validateUserType(userType);
        validateUserId(id);
        return findUserByIdAndType(id, userType);
    }

    private void validateUserType(String userType) {
        if (!"player".equals(userType) && !"manager".equals(userType)) {
            throw new IllegalArgumentException("Invalid user type. Valid options are 'player' or 'manager'.");
        }
    }

    private void validateUserId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
    }

    private Optional<User> findUserByIdAndType(String id, String userType) {
        if ("player".equals(userType)) {
            return playerRepository.findById(id)
                .map(player -> (User)player);
        } else if ("manager".equals(userType)) {
            return managerRepository.findById(id)
                .map(manager -> (User)manager);
        }
        return Optional.empty();
    }
}
