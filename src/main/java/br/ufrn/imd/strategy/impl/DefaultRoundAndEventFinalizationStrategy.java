package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.*;
import br.ufrn.imd.repository.*;
import br.ufrn.imd.strategy.RoundAndEventFinalizationStrategy;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRoundAndEventFinalizationStrategy implements RoundAndEventFinalizationStrategy {

    private final EventRepository eventRepository;
    private final EventResultRepository eventResultRepository;
    private final PlayerRepository playerRepository;
    private final MatchUpdateStrategy matchUpdateStrategy;
    private final DefaultPairingStrategy defaultPairingStrategy;

    @Autowired
    public DefaultRoundAndEventFinalizationStrategy(EventRepository eventRepository, EventResultRepository eventResultRepository,
                                                    PlayerRepository playerRepository, MatchUpdateStrategy matchUpdateStrategy, DefaultPairingStrategy defaultPairingStrategy) {
        this.eventRepository = eventRepository;
        this.eventResultRepository = eventResultRepository;
        this.playerRepository = playerRepository;
        this.matchUpdateStrategy = matchUpdateStrategy;
        this.defaultPairingStrategy = defaultPairingStrategy;
    }

    @Override
    public Event finalizeEvent(String eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("Event not found with ID: " + eventId));

        event.setFinished(true);
        event = eventRepository.save(event);

        List<Player> players = playerRepository.findAllById(event.getPlayerIds());
        if (players.isEmpty()) {
            throw new IllegalStateException("No players found for the event.");
        }

        List<PlayerResult> playerResults = createPlayerResults(players, eventId);
        resetPlayerAttributes(players, eventId);

        saveEventResults(eventId, playerResults);

        return event;
    }

    @Override
    public Event finalizeRound(String eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("Event not found with ID: " + eventId));

        if (event.getCurrentRound() >= event.getNumberOfRounds() + 1) {
            throw new IllegalStateException("All rounds already completed for this event.");
        }

        for (Pairing pairing : event.getPairings()) {
            matchUpdateStrategy.updateMatchResult(pairing);
        }
        if (event.getCurrentRound() < event.getNumberOfRounds()) {
            List<Player> players = playerRepository.findAllById(event.getPlayerIds());
            List<Pairing> newPairings = defaultPairingStrategy.createPairings(players);
            event.setPairings(newPairings);
        }
        if (event.getCurrentRound() < event.getNumberOfRounds()) {
            event.setCurrentRound(event.getCurrentRound() + 1);
        }

        eventRepository.save(event);
        matchUpdateStrategy.updateDeckMatchups(eventId, event.getPairings());

        return event;
    }

    @Override
    public EventResult getEventResultByEventId(String eventId) {
        return eventResultRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event result not found for ID: " + eventId));
    }

    private List<PlayerResult> createPlayerResults(List<Player> players, String eventId) {
        List<PlayerResult> playerResults = new ArrayList<>();
        for (Player player : players) {
            PlayerResult result = new PlayerResult();
            result.setPlayerId(player.getId());
            result.setEventPoints(player.getEventPoints());
            result.setWinrate(player.getWinrate());
            result.setOpponentIds(player.getOpponentIds());
            result.setDeckId(player.getDeckId());
            playerResults.add(result);
        }
        return playerResults;
    }

    private void resetPlayerAttributes(List<Player> players, String eventId) {
        for (Player player : players) {
            player.setRankPoints(player.getRankPoints() + player.getEventPoints());
            player.setEventPoints(0);
            player.setWinrate(0);
            player.setOpponentsMatchWinrate(0);
            player.clearOpponents();
            player.getAppliedEventsId().remove(eventId);
            player.addEventId(eventId);
            playerRepository.save(player);
        }
    }

    private void saveEventResults(String eventId, List<PlayerResult> playerResults) {
        EventResult eventResult = new EventResult();
        eventResult.setEventId(eventId);
        eventResult.setPlayerResults(playerResults);
        eventResultRepository.save(eventResult);
    }
}
