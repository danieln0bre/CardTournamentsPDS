package br.ufrn.imd.controller;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.EventService;
import br.ufrn.imd.service.MatchService;
import br.ufrn.imd.service.PairingService;
import br.ufrn.imd.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventPairingController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PairingService pairingService;
    
    @Autowired
    private MatchService matchService;

    // Retorna o pareamento dos jogadores do evento encontrado pelo ID.
    @GetMapping("/{eventId}/pair")
    public ResponseEntity<?> pairEventPlayers(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();

        if (event.getCurrentRound() >= event.getNumberOfRounds()) {
            return ResponseEntity.badRequest().body("All rounds completed for this event.");
        }

        // Verifica se já existem pareamentos definidos para a rodada atual
        if (!event.getPairings().isEmpty() && event.getPairings().get(0).getResult() == -1) {
            return ResponseEntity.badRequest().body("Pairing already initiated for the current round.");
        }

        event.setCurrentRound(event.getCurrentRound() + 1);
        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        List<Pairing> pairings = pairingService.createPairings(players);
        event.setPairings(pairings);
        eventService.saveEvent(event);
        return ResponseEntity.ok(pairings);
    }
    
    @PostMapping("/{eventId}/finalizeRound")
    public ResponseEntity<?> finalizeRound(@PathVariable String eventId, @RequestBody List<Pairing> pairings) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);

        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();

        if (event.getCurrentRound() >= event.getNumberOfRounds()) {
            return ResponseEntity.badRequest().body("All rounds already completed for this event.");
        }

        // Update results and player points
        pairings.forEach(matchService::updateMatchResult);

        event.setCurrentRound(event.getCurrentRound() + 1); // increment current round

        // Generate new pairings for the next round
        if (event.getCurrentRound() < event.getNumberOfRounds()) {
            List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
            List<Pairing> newPairings = pairingService.createPairings(players);
            event.setPairings(newPairings);
        }

        eventService.saveEvent(event);  // save updated event
        return ResponseEntity.ok("Round finalized and next round prepared.");
    }
}
