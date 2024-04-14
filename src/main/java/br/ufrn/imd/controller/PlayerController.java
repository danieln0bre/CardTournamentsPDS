package br.ufrn.imd.controller;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.EventService;
import br.ufrn.imd.service.PlayerService;
import br.ufrn.imd.service.PlayerWinrateService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private EventService eventService;

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
    
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getPlayerEvents(@PathVariable String id) {
        Optional<Player> playerOptional = playerService.getPlayerById(id);
        if (playerOptional.isPresent()) {
            List<String> eventIds = playerOptional.get().getAppliedEventsId();
            List<Event> events = eventIds.stream()
                                         .map(eventId -> eventService.getEventById(eventId)
                                         .orElseThrow(() -> new RuntimeException("Event not found for ID: " + eventId)))
                                         .collect(Collectors.toList());
            return ResponseEntity.ok(events);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/events/add")
    public ResponseEntity<Player> addEventToPlayer(@PathVariable String id, @RequestBody String eventId) {
        try {
            Player updatedPlayer = playerService.addEventToPlayer(id, eventId.trim());
            return ResponseEntity.ok(updatedPlayer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
