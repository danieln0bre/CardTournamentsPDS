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

    // Cria/registra um novo jogador.
    @PostMapping("/register/player")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        return ResponseEntity.ok(userService.savePlayer(player));
    }

    // Cria/registra um novo manager.
    @PostMapping("/register/manager")
    public ResponseEntity<Manager> registerManager(@RequestBody Manager manager) {
        return ResponseEntity.ok(userService.saveManager(manager));
    }

    // Retorna o usuário encontrado pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id, @RequestParam String userType) {
        User user = userService.getUserById(id, userType);
        
        // Se não existir o usuário.
        if(user == null) {
        	
        	return ResponseEntity.notFound().build();
        }
        
        // Se existir o usuário.
        return ResponseEntity.ok(user);
    }
}
