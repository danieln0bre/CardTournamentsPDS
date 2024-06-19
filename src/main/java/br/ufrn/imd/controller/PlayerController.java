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

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final GeneralRankingService generalRankingService;
    private final PlayerService playerService;
    private final EventService eventService;
    private final DeckService deckService;

    @Autowired
    public PlayerController(GeneralRankingService generalRankingService, PlayerService playerService,
                            EventService eventService, DeckService deckService) {
        this.generalRankingService = generalRankingService;
        this.playerService = playerService;
        this.eventService = eventService;
        this.deckService = deckService;
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

    @PostMapping("/{id}/recalculateWinrates")
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

    @PutMapping("/{id}/events/add")
    public ResponseEntity<String> addEventToPlayer(@PathVariable String id, @RequestBody String eventId) {
        return eventService.getEventById(eventId.trim())
                .map(event -> {
                    checkAndAddEventToPlayer(id, event);
                    return ResponseEntity.ok("Player and Event updated successfully!");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Event not found."));
    }

    private void checkAndAddEventToPlayer(String playerId, Event event) {
        if (event.getPlayerIds().contains(playerId)) {
            throw new IllegalArgumentException("Player is already registered for this event.");
        }
        playerService.addEventToPlayer(playerId, event.getId());
        eventService.addPlayerToEvent(event.getId(), playerId);
    }

    @GetMapping("/rankings")
    public ResponseEntity<List<Player>> getGeneralRankings() {
        List<Player> rankedPlayers = generalRankingService.getRankedPlayersByRankPoints();
        return rankedPlayers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(rankedPlayers);
    }

    @PutMapping("/{id}/updateDeck")
    public ResponseEntity<?> updatePlayerDeck(@PathVariable String id, @RequestBody String deckId) {
        return playerService.getPlayerById(id)
                .map(player -> {
                    Deck deck = deckService.getDeckById(deckId);
                    if (deck == null) {
                        return ResponseEntity.badRequest().body("Deck not found");
                    }
                    player.setDeckId(deckId);
                    playerService.savePlayer(player);
                    return ResponseEntity.ok("Deck updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/decks/{deckId}")
    public ResponseEntity<Deck> getDeckById(@PathVariable String deckId) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck != null) {
            return ResponseEntity.ok(deck);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/winning-decks")
    public ResponseEntity<List<Deck>> getWinningDecks() {
        List<Deck> decks = deckService.getAllWinningDecks();
        return ResponseEntity.ok(decks);
    }
}
