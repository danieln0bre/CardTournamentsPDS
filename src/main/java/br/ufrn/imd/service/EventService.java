package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.repository.EventRepository;
import br.ufrn.imd.repository.EventResultRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import br.ufrn.imd.strategy.RoundAndEventFinalizationStrategy;
import br.ufrn.imd.strategy.StatisticsGenerationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService {

    private final PairingService pairingService;
    private final MatchUpdateStrategy matchUpdateStrategy;
    private final RoundAndEventFinalizationStrategy roundAndEventFinalizationStrategy;
    private final StatisticsGenerationStrategy statisticsGenerationStrategy;
    private final EventRankingService eventRankingService;
    private final EventRepository eventRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    @Autowired
    public EventService(PairingService pairingService, MatchUpdateStrategy matchUpdateStrategy,
                        RoundAndEventFinalizationStrategy roundAndEventFinalizationStrategy,
                        StatisticsGenerationStrategy statisticsGenerationStrategy, EventRankingService eventRankingService,
                        EventRepository eventRepository, PlayerRepository playerRepository, PlayerService playerService) {
        this.pairingService = pairingService;
        this.matchUpdateStrategy = matchUpdateStrategy;
        this.roundAndEventFinalizationStrategy = roundAndEventFinalizationStrategy;
        this.statisticsGenerationStrategy = statisticsGenerationStrategy;
        this.eventRankingService = eventRankingService;
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
        return roundAndEventFinalizationStrategy.finalizeEvent(eventId);
    }

    public EventResult getEventResultByEventId(String eventId) {
        return roundAndEventFinalizationStrategy.getEventResultByEventId(eventId);
    }

    public List<PlayerResult> getEventResultRanking(String eventId) {
        EventResult eventResult = getEventResultByEventId(eventId);
        return eventRankingService.sortByResultEventPoints(eventResult.getPlayerResults());
    }

    public void updateMatchResult(Pairing pairing) {
        matchUpdateStrategy.updateMatchResult(pairing);
    }

    public Event finalizeRound(String eventId) {
        return roundAndEventFinalizationStrategy.finalizeRound(eventId);
    }

    public Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId) {
        EventResult eventResult = getEventResultByEventId(eventId);
        return statisticsGenerationStrategy.generateStatistics(eventResult);
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
