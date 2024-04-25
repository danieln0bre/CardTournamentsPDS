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

    // Realiza o pareamento e retorna.
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
        event.setCurrentRound(1);  // O evento começa no round 1.
        eventService.saveEvent(event);
        return ResponseEntity.ok(pairings);
    }

    // Finaliza a rodada atual.
    @PostMapping("/{eventId}/finalizeRound")
    public ResponseEntity<?> finalizeRound(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        
        // Se o round atual do evento for maior ou igual ao número de rounds do evento.

        if (event.getCurrentRound() >= event.getNumberOfRounds()) {
            return ResponseEntity.badRequest().body("All rounds already completed for this event.");
        }

        // Se o round atual do evento for menor que o número de rounds do evento.
        
        event.getPairings().forEach(matchService::updateMatchResult);  // Atualiza resultados para o round atual.

        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        List<Pairing> newPairings = pairingService.createPairings(players);
        event.setPairings(newPairings);
        event.setCurrentRound(event.getCurrentRound() + 1); // Prepara para o próximo round.

        eventService.saveEvent(event);
        return ResponseEntity.ok("Round finalized and next round prepared, if applicable.");
    }
}