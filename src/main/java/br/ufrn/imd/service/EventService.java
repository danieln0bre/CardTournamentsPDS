package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.model.TeamResult;
import br.ufrn.imd.repository.EventRepository;
import br.ufrn.imd.repository.EventResultRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import br.ufrn.imd.strategy.RoundAndEventFinalizationStrategy;
import br.ufrn.imd.strategy.StatisticsGenerationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        // Preencher EventResult com os dados do evento
        EventResult eventResult = new EventResult();
        eventResult.setEventId(eventId);
        List<TeamResult> teamResults = new ArrayList<>();
        
        List<String> teamIds = event.getEntityIds(); // Obter os IDs dos times do evento
        
        // Preencher teamResults com todos os TeamResults e seus opponentTeamIds
        for (String teamId : teamIds) {
            TeamResult teamResult = new TeamResult();
            teamResult.setTeamId(teamId);
            // Preencher opponentTeamIds com os IDs dos times oponentes
            List<String> opponentTeamIds = new ArrayList<>();
            for (String opponentId : teamIds) {
                if (!opponentId.equals(teamId)) {
                    opponentTeamIds.add(opponentId);
                }
            }
            teamResult.setOpponentTeamIds(opponentTeamIds);
            teamResults.add(teamResult);
        }
        eventResult.setTeamResults(teamResults);
        return eventResult;
    }

    public List<PlayerResult> getEventResultRanking(String eventId) {
        EventResult eventResult = getEventResultByEventId(eventId);
        return eventRankingService.sortByResultEventPoints(eventResult.getTeamResults());
    }

    public void updateMatchResult(Pairing pairing) {
        matchUpdateStrategy.updateMatchResult(pairing);
    }
    
    public void addTeamToEvent(String eventId, String teamId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            List<String> entityIds = new ArrayList<>(event.getEntityIds()); // Garante que a coleção é mutável
            entityIds.add(teamId);
            event.setEntityIds(entityIds);
            eventRepository.save(event);
        } else {
            throw new NoSuchElementException("Event not found");
        }
    }

    public Event finalizeRound(String eventId) {
        return roundAndEventFinalizationStrategy.finalizeRound(eventId);
    }

    public Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId) {
        try {
            EventResult eventResult = getEventResultByEventId(eventId);
            System.out.println("EventResult encontrado para o Event ID: " + eventId);
            return statisticsGenerationStrategy.generateStatistics(eventResult);
        } catch (Exception e) {
            System.err.println("Erro ao gerar estatísticas: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    public Event addPlayerToEvent(String eventId, String playerId) {
        Event event = getEventById(eventId).orElseThrow(() ->
            new IllegalArgumentException("Event not found with ID: " + eventId));
        
        if (event.getEntityIds().contains(playerId)) {
            throw new IllegalArgumentException("Player already added to the event.");
        }

        event.addEntityId(playerId);
        return eventRepository.save(event);
    }
}
