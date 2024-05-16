package br.ufrn.imd.controller;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.service.UserService;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // Registers a new player.
    @PostMapping("/register/player")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        Player savedPlayer = userService.savePlayer(player);
        return ResponseEntity.ok(savedPlayer);
    }

    // Registers a new manager.
    @PostMapping("/register/manager")
    public ResponseEntity<Manager> registerManager(@RequestBody Manager manager) {
        Manager savedManager = userService.saveManager(manager);
        return ResponseEntity.ok(savedManager);
    }

    // Retrieves a user by ID and type.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id, @RequestParam String userType) {
        return userService.getUserById(id, userType)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    // Login method
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent() && userService.checkPassword(userOpt.get(), password)) {
            User user = userOpt.get();
            switch (user.getRole()) {
                case ROLE_PLAYER:
                    return ResponseEntity.ok("Logged in as Player");
                case ROLE_MANAGER:
                    return ResponseEntity.ok("Logged in as Manager");
                default:
                    return ResponseEntity.status(403).body("Unknown role");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
