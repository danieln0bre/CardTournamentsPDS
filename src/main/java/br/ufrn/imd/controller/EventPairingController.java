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

    @GetMapping("/{eventId}/pair")
    public ResponseEntity<?> pairEventPlayers(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        if (event.getCurrentRound() != 0) {
            return ResponseEntity.badRequest().body("Pairing can only be initiated at the start.");
        }

        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        List<Pairing> pairings = pairingService.createPairings(players);

        event.setPairings(pairings);
        event.setCurrentRound(1);  // Start the event at round 1
        eventService.saveEvent(event);
        return ResponseEntity.ok(pairings);
    }

    @PostMapping("/{eventId}/finalizeRound")
    public ResponseEntity<?> finalizeRound(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        if (event.getCurrentRound() >= event.getNumberOfRounds()) {
            return ResponseEntity.badRequest().body("All rounds already completed for this event.");
        }

        event.getPairings().forEach(matchService::updateMatchResult);  // Update results for current round

        if (event.getCurrentRound() < event.getNumberOfRounds()) {
            List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
            List<Pairing> newPairings = pairingService.createPairings(players);
            event.setPairings(newPairings);
            event.setCurrentRound(event.getCurrentRound() + 1); // Prepare for the next round
        } else {
            event.setCurrentRound(event.getCurrentRound() + 1); // Mark as complete
        }

        eventService.saveEvent(event);
        return ResponseEntity.ok("Round finalized and next round prepared, if applicable.");
    }
}