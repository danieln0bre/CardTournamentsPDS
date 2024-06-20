package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.repository.EventRepository;
import br.ufrn.imd.repository.EventResultRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.strategy.EventServiceStrategy;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService {

    private final PairingStrategy pairingStrategy;
    private final MatchUpdateStrategy matchUpdateStrategy;
    private final EventServiceStrategy eventServiceStrategy;
    private final EventRepository eventRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    @Autowired
    public EventService(PairingStrategy pairingStrategy, MatchUpdateStrategy matchUpdateStrategy,
                        EventServiceStrategy eventServiceStrategy, EventRepository eventRepository,
                        PlayerRepository playerRepository, PlayerService playerService) {
        this.pairingStrategy = pairingStrategy;
        this.matchUpdateStrategy = matchUpdateStrategy;
        this.eventServiceStrategy = eventServiceStrategy;
        this.eventRepository = eventRepository;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    public Event saveEvent(Event event) {
        validateEventDetails(event);
        return eventRepository.save(event);
    }

    private void validateEventDetails(Event event) {
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty.");
        }
        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Event location cannot be empty.");
        }
    }

    public Optional<Event> getEventByName(String name) {
        return eventRepository.findByName(name);
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public Event finalizeEvent(String eventId) {
        return eventServiceStrategy.finalizeEvent(eventId);
    }

    public EventResult getEventResultByEventId(String eventId) {
        return eventServiceStrategy.getEventResultByEventId(eventId);
    }

    public List<PlayerResult> getEventResultRanking(String eventId) {
        return eventServiceStrategy.getEventResultRanking(eventId);
    }

    public void updateMatchResult(Pairing pairing) {
        matchUpdateStrategy.updateMatchResult(pairing);
    }

    public Event finalizeRound(String eventId) {
        Event event = getEventById(eventId).orElseThrow(() ->
            new IllegalArgumentException("Event not found with ID: " + eventId));

        if (event.getCurrentRound() >= event.getNumberOfRounds()) {
            throw new IllegalStateException("All rounds already completed for this event.");
        }

        event.getPairings().forEach(matchUpdateStrategy::updateMatchResult);

        if (event.getCurrentRound() < event.getNumberOfRounds()) {
            List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
            List<Pairing> newPairings = pairingStrategy.createPairings(players);
            event.setPairings(newPairings);
            event.setCurrentRound(event.getCurrentRound() + 1);
        }

        eventRepository.save(event);
        matchUpdateStrategy.updateDeckMatchups(eventId, event.getPairings());

        return event;
    }

    public Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId) {
        EventResult eventResult = getEventResultByEventId(eventId);
        return matchUpdateStrategy.getDeckMatchupStatistics(eventResult);
    }

    public Event addPlayerToEvent(String eventId, String playerId) {
        Event event = getEventById(eventId).orElseThrow(() ->
            new IllegalArgumentException("Event not found with ID: " + eventId));
        
        if (event.getPlayerIds().contains(playerId)) {
            throw new IllegalArgumentException("Player already added to the event.");
        }

        event.addPlayerId(playerId);
        return eventRepository.save(event);
    }
}
