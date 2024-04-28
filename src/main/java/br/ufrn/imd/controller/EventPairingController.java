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
    
    // Método para iniciar o evento.
    @PostMapping("/{eventId}/start")
    public ResponseEntity<?> startEvent(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        if (event.getHasStarted()) {
            return ResponseEntity.badRequest().body("Event has already started.");
        }

        if (!playerService.allPlayersHaveDecks(event.getPlayerIds())) {
            return ResponseEntity.badRequest().body("Not all players have registered decks.");
        }

        event.setHasStarted(true);
        eventService.saveEvent(event);
        return ResponseEntity.ok("Event started successfully.");
    }

    // Metodo para gerar os pairings	
    @PostMapping("/{eventId}/generatePairings")
    public ResponseEntity<?> generatePairings(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        if (!event.getHasStarted()) {
            return ResponseEntity.badRequest().body("Event has not started yet.");
        }

        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        List<Pairing> pairings = pairingService.createPairings(players);

        event.setPairings(pairings);
        eventService.saveEvent(event);
        return ResponseEntity.ok("Pairings generated successfully.");
    }
    
    // Método para retornar os pairings atuais de um evento.
    @GetMapping("/{eventId}/pairings")
    public ResponseEntity<?> getEventPairings(@PathVariable String eventId) {
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        return ResponseEntity.ok(event.getPairings());
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