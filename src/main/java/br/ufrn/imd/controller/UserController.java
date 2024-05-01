package br.ufrn.imd.controller;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}
