package br.ufrn.imd.controller;

import br.ufrn.imd.model.GameObject;
import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final GeneralRankingService generalRankingService;
    private final PlayerService playerService;
    private final EventService eventService;
    private final GameObjectService gameObjectService;
    private final TeamService teamService;

    @Autowired
    public PlayerController(GeneralRankingService generalRankingService, PlayerService playerService,
                            EventService eventService, GameObjectService gameObjectService, TeamService teamService) {
        this.generalRankingService = generalRankingService;
        this.playerService = playerService;
        this.eventService = eventService;
        this.gameObjectService = gameObjectService;
        this.teamService = teamService;
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Player> updatePlayerInfo(@PathVariable String id, @RequestBody Player userDetails) {
        return playerService.getPlayerById(id)
                .map(player -> {
                    updatePlayerFields(player, userDetails);
                    return ResponseEntity.ok(playerService.updatePlayer(id, player));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void updatePlayerFields(Player player, Player userDetails) {
        Optional.ofNullable(userDetails.getEmail()).ifPresent(player::setEmail);
        Optional.ofNullable(userDetails.getUsername()).ifPresent(player::setUsername);
        Optional.ofNullable(userDetails.getPassword()).ifPresent(player::setPassword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/recalculate-winrates")
    public ResponseEntity<Player> recalculateWinrates(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok(playerService.recalculateWinrates(id)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getPlayerEvents(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok(fetchEvents(player.getAppliedEventsId())))
                .orElse(ResponseEntity.notFound().build());
    }

    private List<Event> fetchEvents(List<String> eventIds) {
        return eventIds.stream()
                .map(eventId -> eventService.getEventById(eventId)
                        .orElseThrow(() -> new RuntimeException("Event not found for ID: " + eventId)))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/entities/add")
    public ResponseEntity<String> addEventToEntity(@PathVariable String id, @RequestBody String eventId) {
        return eventService.getEventById(eventId.trim())
                .map(event -> {
                    checkAndAddEventToEntity(id, event);
                    return ResponseEntity.ok("Entity and Event updated successfully!");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Event not found."));
    }

    private void checkAndAddEventToEntity(String entityId, Event event) {
        if (event.getEntityIds().contains(entityId)) {
            throw new IllegalArgumentException("Entity is already registered for this event.");
        }

        if (playerService.existsById(entityId)) {
            playerService.addEventToPlayer(entityId, event.getId());
            eventService.addPlayerToEvent(event.getId(), entityId);
        } else if (teamService.existsById(entityId)) {
            teamService.addEventToTeam(entityId, event.getId());
            eventService.addTeamToEvent(event.getId(), entityId);
        } else {
            throw new IllegalArgumentException("Entity not found.");
        }
    }

    @GetMapping("/rankings")
    public ResponseEntity<List<Player>> getGeneralRankings() {
        List<Player> rankedPlayers = generalRankingService.getRankedPlayersByRankPoints();
        return rankedPlayers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(rankedPlayers);
    }

    @PutMapping("/{id}/update-game-object")
    public ResponseEntity<?> updatePlayerGameObject(@PathVariable String id, @RequestBody String deckId) {
        return playerService.getPlayerById(id)
                .map(player -> {
                    GameObject deck = gameObjectService.getGameObjectById(deckId);
                    if (deck == null) {
                        return ResponseEntity.badRequest().body("GameObject not found");
                    }
                    player.setGameObjectId(deckId);
                    playerService.savePlayer(player);
                    return ResponseEntity.ok("Deck updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/game-objects/{gameObjectId}")
    public ResponseEntity<GameObject> getGameObjectById(@PathVariable String deckId) {
        GameObject deck = gameObjectService.getGameObjectById(deckId);
        if (deck != null) {
            return ResponseEntity.ok(deck);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/game-objects")
    public ResponseEntity<List<GameObject>> getGameObjects() {
        List<GameObject> decks = gameObjectService.getAllGameObjects();
        return ResponseEntity.ok(decks);
    }
}
