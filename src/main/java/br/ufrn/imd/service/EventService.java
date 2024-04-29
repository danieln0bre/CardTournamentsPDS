package br.ufrn.imd.service;

import br.ufrn.imd.model.Deck;
import br.ufrn.imd.model.Event;
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

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private PlayerService playerService;

    public Event saveEvent(Event event) {
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty.");
        }
        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Event location cannot be empty.");
        }
        // Não é necessária validação de formato para a data
        return eventRepository.save(event);
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
        return getEventById(id).map(event -> {
            event.setName(eventDetails.getName());
            event.setDate(eventDetails.getDate());
            event.setLocation(eventDetails.getLocation());
            event.setNumberOfRounds(eventDetails.getNumberOfRounds());
            event.setPlayerIds(eventDetails.getPlayerIds());
            return eventRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found!"));
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

        // Sorting players to determine positions
        List<Player> sortedPlayers = EventRankingService.sortByEventPoints(players);
        Map<String, Integer> playerPositions = new HashMap<>();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            playerPositions.put(sortedPlayers.get(i).getId(), i + 1);  // Store position by player ID
        }

        players.forEach(player -> {
            int earnedEventPoints = player.getEventPoints();
            player.setRankPoints(player.getRankPoints() + earnedEventPoints);
            player.setEventPoints(0);
            player.setWinrate(0);
            player.setOpponentsMatchWinrate(0);
            player.clearOpponents();
            player.getAppliedEventsId().remove(eventId);

            // Add the event ID to the player's history
            player.addEventId(eventId);

            // Update the deck
            if (player.getDeck() != null) {
                Integer currentPosition = playerPositions.get(player.getId());
                if (currentPosition != null) {
                    player.getDeck().getPositionFrequencies().merge(currentPosition, 1, Integer::sum);
                }
            }
        });

        playerService.saveAll(players);  // Saves the player updates

        return event;
    }

}
