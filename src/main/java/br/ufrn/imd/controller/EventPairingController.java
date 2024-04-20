package br.ufrn.imd.controller;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.EventService;
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

    @GetMapping("/{eventId}/pair")
    public ResponseEntity<?> pairEventPlayers(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            if (event.getCurrentRound() < event.getNumberOfRounds()) {
                event.setCurrentRound(event.getCurrentRound() + 1);
                List<Player> players = playerService.getPlayersByIds(event.getPlayerIds()); // Assumes getPlayersByIds takes List<String>
                List<Pairing> pairings = pairingService.createPairings(players);
                event.setPairings(pairings);
                eventService.saveEvent(event); // Save the event with the updated current round
                return ResponseEntity.ok(pairings);
            } else {
                return ResponseEntity.badRequest().body("All rounds completed for this event.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
