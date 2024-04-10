package br.ufrn.imd.controller;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.PlayerService;
import br.ufrn.imd.service.PlayerWinrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerWinrateService winrateService;

    @PutMapping("/{id}/update")
    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Player playerDetails) {
        Player updatedPlayer = playerService.updatePlayer(id, playerDetails);
        return ResponseEntity.ok(updatedPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/recalculateWinrates")
    public ResponseEntity<Player> recalculateWinrates(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(player -> {
                    Player updatedPlayer = winrateService.calculateWinRates(player);
                    return ResponseEntity.ok(updatedPlayer);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
