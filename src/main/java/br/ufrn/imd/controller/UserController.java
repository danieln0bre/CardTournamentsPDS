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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        System.out.println("Login attempt with username: " + username);

        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found with username: " + username);

            if (user.getPassword().equals(password)) {
                System.out.println("Password check passed for username: " + username);

                switch (user.getRole()) {
                    case ROLE_PLAYER:
                        System.out.println("User logged in as Player");
                        return ResponseEntity.ok("Logged in as Player");
                    case ROLE_MANAGER:
                        System.out.println("User logged in as Manager");
                        return ResponseEntity.ok("Logged in as Manager");
                    default:
                        System.out.println("Unknown role for user: " + username);
                        return ResponseEntity.status(403).body("Unknown role");
                }
            } else {
                System.out.println("Password check failed for username: " + username);
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } else {
            System.out.println("User not found with username: " + username);
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
