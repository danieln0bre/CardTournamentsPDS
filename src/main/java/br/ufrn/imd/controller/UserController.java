package br.ufrn.imd.controller;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.UserService;
import br.ufrn.imd.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/player")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        return ResponseEntity.ok(userService.savePlayer(player));
    }

    @PostMapping("/register/manager")
    public ResponseEntity<Manager> registerManager(@RequestBody Manager manager) {
        return ResponseEntity.ok(userService.saveManager(manager));
    }

    // Consider adding a userType parameter or similar if needed
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id, @RequestParam String userType) {
        User user = userService.getUserById(id, userType);
        
        if(user == null) {
        	
        	return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(user);
    }
}
