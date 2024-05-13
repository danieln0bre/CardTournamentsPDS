package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService {

    private final PairingService pairingService;
    private final EventRepository eventRepository;
    private final MatchService matchService;
    private final PlayerService playerService;

    @Autowired
    public EventService(PairingService pairingService, EventRepository eventRepository,
                        MatchService matchService, PlayerService playerService) {
        this.pairingService = pairingService;
        this.eventRepository = eventRepository;
        this.matchService = matchService;
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
    
    public Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId) {
        Event event = eventRepository.findById(eventId)
                                     .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        return matchService.getDeckMatchupStatistics(eventId);
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

        event.setFinished(true);  // Marks the event as finished
        event = eventRepository.save(event);  // Saves the final state of the event

        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        if (players.isEmpty()) {
            throw new IllegalStateException("No players found for the event.");
        }

        // Use sortByEventPoints from EventRankingService to determine the player positions
        List<Player> sortedPlayers = EventRankingService.sortByEventPoints(players);
        Map<String, Integer> playerPositions = new HashMap<>();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            playerPositions.put(sortedPlayers.get(i).getId(), i + 1);
        }

        players.forEach(player -> {
            int earnedEventPoints = player.getEventPoints();
            player.setRankPoints(player.getRankPoints() + earnedEventPoints);
            player.setEventPoints(0);
            player.setWinrate(0);
            player.setOpponentsMatchWinrate(0);
            player.clearOpponents();
            player.getAppliedEventsId().remove(eventId);
            player.addEventId(eventId);

            if (player.getDeck() != null) {
                Integer currentPosition = playerPositions.get(player.getId());
                if (currentPosition != null) {
                    // Update the deck's position frequencies based on the current position
                    player.getDeck().getPositionFrequencies().merge(currentPosition, 1, Integer::sum);
                    // Save changes to deck if necessary, depending on your persistence logic
                }
            }
        });

        playerService.saveAll(players);

        return event;
    }


    public Event finalizeRound(String eventId) {
        Event event = getEventById(eventId).orElseThrow(() ->
            new IllegalArgumentException("Event not found with ID: " + eventId));

        if (event.getCurrentRound() >= event.getNumberOfRounds()) {
            throw new IllegalStateException("All rounds already completed for this event.");
        }

        event.getPairings().forEach(matchService::updateMatchResult);

        List<Player> players = playerService.getPlayersByIds(event.getPlayerIds());
        List<Pairing> newPairings = pairingService.createPairings(players);
        event.setPairings(newPairings);
        event.setCurrentRound(event.getCurrentRound() + 1);

        return eventRepository.save(event);
    }
}
