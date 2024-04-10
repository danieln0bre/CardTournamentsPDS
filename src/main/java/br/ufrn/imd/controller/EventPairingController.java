package br.ufrn.imd.controller;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.EventService;
import br.ufrn.imd.service.PairingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PairingService pairingService;

    @GetMapping("/{eventId}/pair")
    public List<Pairing> pairEventPlayers(@PathVariable Long eventId) {
        Optional<Event> event = eventService.getEventById(eventId);
        if (event.isPresent()) {
            List<Player> eventPlayers = event.get().getPlayers();
            if (event.get().getCurrentRound() < event.get().getNumberOfRounds()) {
                event.get().setCurrentRound(event.get().getCurrentRound() + 1);
                eventService.saveEvent(event.get()); // Update the event with new round information
                return pairingService.createPairings(new ArrayList<>(eventPlayers));
            } else {
                throw new RuntimeException("All rounds completed for this event.");
            }
        } else {
            throw new RuntimeException("Event not found with id: " + eventId);
        }
    }
}
