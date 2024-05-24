package br.ufrn.imd.controller;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.User;
import br.ufrn.imd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/player")
    public ResponseEntity<?> registerPlayer(@RequestBody Player player) {
        try {
            Player savedPlayer = userService.savePlayer(player);
            return ResponseEntity.ok(savedPlayer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@RequestBody Manager manager) {
        try {
            Manager savedManager = userService.saveManager(manager);
            return ResponseEntity.ok(savedManager);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id, @RequestParam String userType) {
        return userService.getUserById(id, userType)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
}
