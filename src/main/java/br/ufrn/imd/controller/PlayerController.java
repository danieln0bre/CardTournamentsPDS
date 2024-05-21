package br.ufrn.imd.controller;

import br.ufrn.imd.model.Deck;
import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// RestController for managing Player-related operations.
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    // Service dependencies for various player-related operations.
    private final GeneralRankingService generalRankingService;
    private final PlayerService playerService;
    private final EventService eventService;
    private final PlayerWinrateService winrateService;
    private final DeckService deckService;

    // Autowired constructor for dependency injection.
    @Autowired
    public PlayerController(GeneralRankingService generalRankingService, PlayerService playerService,
                            EventService eventService, PlayerWinrateService winrateService, DeckService deckService) {
        this.generalRankingService = generalRankingService;
        this.playerService = playerService;
        this.eventService = eventService;
        this.winrateService = winrateService;
        this.deckService = deckService;
    }

    // Updates a player's information.
    @PutMapping("/{id}/update")
    public ResponseEntity<Player> updatePlayerInfo(@PathVariable String id, @RequestBody Player userDetails) {
        return playerService.getPlayerById(id)
                .map(player -> {
                    updatePlayerFields(player, userDetails);
                    return ResponseEntity.ok(playerService.updatePlayer(id, player));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Helper method to update player fields if new values are provided.
    private void updatePlayerFields(Player player, Player userDetails) {
        Optional.ofNullable(userDetails.getEmail()).ifPresent(player::setEmail);
        Optional.ofNullable(userDetails.getUsername()).ifPresent(player::setUsername);
        Optional.ofNullable(userDetails.getPassword()).ifPresent(player::setPassword);
    }

    // Retrieves a player by their ID.
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Recalculates a player's win rates.
    @PostMapping("/{id}/recalculateWinrates")
    public ResponseEntity<Player> recalculateWinrates(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok(winrateService.calculateWinRates(player)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Retrieves the events a player is associated with.
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getPlayerEvents(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok(fetchEvents(player.getAppliedEventsId())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Helper method to fetch events based on event IDs.
    private List<Event> fetchEvents(List<String> eventIds) {
        return eventIds.stream()
                .map(eventId -> eventService.getEventById(eventId)
                        .orElseThrow(() -> new RuntimeException("Event not found for ID: " + eventId)))
                .collect(Collectors.toList());
    }

    // Adds an event to a player's profile.
    @PutMapping("/{id}/events/add")
    public ResponseEntity<String> addEventToPlayer(@PathVariable String id, @RequestBody String eventId) {
        return eventService.getEventById(eventId.trim())
                .map(event -> {
                    checkAndAddEventToPlayer(id, event);
                    return ResponseEntity.ok("Player and Event updated successfully!");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Event not found."));
    }

    // Helper method to check if an event can be added to a player and update both player and event.
    private void checkAndAddEventToPlayer(String playerId, Event event) {
        if (event.getPlayerIds().contains(playerId)) {
            throw new IllegalArgumentException("Player is already registered for this event.");
        }
        playerService.addEventToPlayer(playerId, event.getId());
        eventService.addPlayerToEvent(event.getId(), playerId);
    }

    // Retrieves the general rankings of players.
    @GetMapping("/rankings")
    public ResponseEntity<List<Player>> getGeneralRankings() {
        List<Player> rankedPlayers = generalRankingService.getRankedPlayersByRankPoints();
        return rankedPlayers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(rankedPlayers);
    }

    // Updates a player's deck.
    @PutMapping("/{id}/updateDeck")
    public ResponseEntity<?> updatePlayerDeck(@PathVariable String id, @RequestBody String deckId) {
        return playerService.getPlayerById(id)
                .map(player -> updateDeckForPlayer(player, deckId))
                .orElse(ResponseEntity.notFound().build());
    }

    // Helper method to update a player's deck.
    private ResponseEntity<?> updateDeckForPlayer(Player player, String deckId) {
        Deck deck = deckService.findDeckById(deckId).orElseThrow(() -> new IllegalArgumentException("Deck ID not found."));
        player.setDeck(deck);
        playerService.updatePlayer(player.getId(), player);
        return ResponseEntity.ok(player.getDeckId());
    }
}
