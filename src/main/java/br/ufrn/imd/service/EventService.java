package br.ufrn.imd.service;

import br.ufrn.imd.model.Deck;
import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.repository.EventRepository;
import br.ufrn.imd.repository.EventResultRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.strategy.EventRankingStrategy;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService {

    private final PairingStrategy pairingStrategy;
    private final MatchUpdateStrategy matchUpdateStrategy;
    private final EventRankingStrategy eventRankingStrategy;
    private final EventRepository eventRepository;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    private final EventResultRepository eventResultRepository;
    
    @Autowired
    public EventService(PairingStrategy pairingStrategy, MatchUpdateStrategy matchUpdateStrategy, EventRankingStrategy eventRankingStrategy, 
                        EventRepository eventRepository, PlayerService playerService, PlayerRepository playerRepository, 
                        EventResultRepository eventResultRepository) {
        this.pairingStrategy = pairingStrategy;
        this.matchUpdateStrategy = matchUpdateStrategy;
        this.eventRankingStrategy = eventRankingStrategy;
        this.eventRepository = eventRepository;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.eventResultRepository = eventResultRepository;
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

    public Event updateEvent(String id, Event eventDetails) {
        return getEventById(id).map(event -> updateEventDetails(event, eventDetails))
                               .orElseThrow(() -> new RuntimeException("Event not found!"));
    }

    private Event updateEventDetails(Event event, Event eventDetails) {
        event.setName(eventDetails.getName());
        event.setDate(eventDetails.getDate());
        event.setLocation(eventDetails.getLocation());
        event.setNumberOfRounds(eventDetails.getNumberOfRounds());
        event.setPlayerIds(eventDetails.getPlayerIds());
        return eventRepository.save(event);
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

    public Event finalizeEvent(String eventId) {
        Event event = getEventById(eventId).orElseThrow(() ->
            new IllegalArgumentException("Event not found with ID: " + eventId));

        event.setFinished(true);
        event = eventRepository.save(event);

        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        if (players.isEmpty()) {
            throw new IllegalStateException("No players found for the event.");
        }

        List<PlayerResult> playerResults = new ArrayList<>();
        players.forEach(player -> {
            PlayerResult result = new PlayerResult();
            result.setPlayerId(player.getId());
            result.setEventPoints(player.getEventPoints());
            result.setWinrate(player.getWinrate());
            result.setOpponentIds(player.getOpponentIds());
            result.setDeckId(player.getDeckId());
            playerResults.add(result);

            // Reset player attributes
            player.setRankPoints(player.getRankPoints() + player.getEventPoints());
            player.setEventPoints(0);
            player.setWinrate(0);
            player.setOpponentsMatchWinrate(0);
            player.clearOpponents();
            player.getAppliedEventsId().remove(eventId);
            player.addEventId(eventId);
            playerRepository.save(player);
        });

        // Save event results
        EventResult eventResult = new EventResult();
        eventResult.setEventId(eventId);
        eventResult.setPlayerResults(playerResults);
        eventResultRepository.save(eventResult);

        return event;
    }
    
    public EventResult getEventResultByEventId(String eventId) {
        return eventResultRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event result not found for ID: " + eventId));
    }

    public List<PlayerResult> getEventResultRanking(String eventId) {
        EventResult eventResult = getEventResultByEventId(eventId);
        return eventRankingStrategy.rankPlayerResults(eventResult.getPlayerResults());
    }

    public Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId) {
        EventResult eventResult = getEventResultByEventId(eventId);
        return matchUpdateStrategy.getDeckMatchupStatistics(eventResult);
    }

    public Event finalizeRound(String eventId) {
        Event event = getEventById(eventId).orElseThrow(() ->
            new IllegalArgumentException("Event not found with ID: " + eventId));

        if (event.getCurrentRound() >= event.getNumberOfRounds()+1) {
            throw new IllegalStateException("All rounds already completed for this event.");
        }

        event.getPairings().forEach(matchUpdateStrategy::updateMatchResult);
        if(event.getCurrentRound() < event.getNumberOfRounds()) {
	        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
	        List<Pairing> newPairings = pairingStrategy.createPairings(players);
	        event.setPairings(newPairings);
        }
        if(event.getCurrentRound() < event.getNumberOfRounds()){
        	event.setCurrentRound(event.getCurrentRound() + 1);
        }

        eventRepository.save(event);
        matchUpdateStrategy.updateDeckMatchups(eventId, event.getPairings());

        return event;
    }
}
