package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.*;
import br.ufrn.imd.repository.*;
import br.ufrn.imd.service.TeamService;
import br.ufrn.imd.strategy.RoundAndEventFinalizationStrategy;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultTeamRoundAndEventFinalizationStrategy implements RoundAndEventFinalizationStrategy {

    private final EventRepository eventRepository;
    private final EventResultRepository eventResultRepository;
    private final TeamRepository teamRepository;
    private final MatchUpdateStrategy matchUpdateStrategy;
    private final PairingStrategy pairingStrategy;
    private final TeamService teamService;

    @Autowired
    public DefaultTeamRoundAndEventFinalizationStrategy(EventRepository eventRepository, EventResultRepository eventResultRepository,
                                                        TeamRepository teamRepository, MatchUpdateStrategy matchUpdateStrategy,
                                                        PairingStrategy pairingStrategy, TeamService teamService) {
        this.eventRepository = eventRepository;
        this.eventResultRepository = eventResultRepository;
        this.teamRepository = teamRepository;
        this.matchUpdateStrategy = matchUpdateStrategy;
        this.pairingStrategy = pairingStrategy;
        this.teamService = teamService;
    }

    @Override
    public Event finalizeEvent(String eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("Event not found with ID: " + eventId));

        event.setFinished(true);
        event = eventRepository.save(event);

        List<Team> teams = teamRepository.findAllById(event.getEntityIds());
        if (teams.isEmpty()) {
            throw new IllegalStateException("No teams found for the event.");
        }

        List<TeamResult> teamResults = createTeamResults(teams, eventId);
        resetTeamAttributes(teams, eventId);

        saveEventResults(eventId, teamResults);

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
            List<Team> teams = teamRepository.findAllById(event.getEntityIds());
            List<Pairing> newPairings = pairingStrategy.createPairings(teams);
            event.setPairings(newPairings);
        }
        if (event.getCurrentRound() < event.getNumberOfRounds()) {
            event.setCurrentRound(event.getCurrentRound() + 1);
        }

        eventRepository.save(event);
        matchUpdateStrategy.updateGameObjectMatchups(eventId, event.getPairings());

        return event;
    }

    @Override
    public EventResult getEventResultByEventId(String eventId) {
        return eventResultRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event result not found for ID: " + eventId));
    }

    private List<TeamResult> createTeamResults(List<Team> teams, String eventId) {
        List<TeamResult> teamResults = new ArrayList<>();
        for (Team team : teams) {
            TeamResult result = new TeamResult();
            result.setTeamId(team.getId());
            result.setEventPoints(teamService.getEventPoints(team));
            result.setWinrate(teamService.getWinrate(team));
            teamResults.add(result);
        }
        return teamResults;
    }

    private void resetTeamAttributes(List<Team> teams, String eventId) {
        for (Team team : teams) {
            for (Player player : teamService.getPlayers(team)) {
                player.setRankPoints(player.getRankPoints() + team.getEventPoints());
                player.setEventPoints(0);
                player.setWinrate(0);
                player.setOpponentsMatchWinrate(0);
                player.clearOpponents();
                player.getAppliedEventsId().remove(eventId);
                player.addEventId(eventId);
            }
            teamRepository.save(team);
        }
    }

    private void saveEventResults(String eventId, List<TeamResult> teamResults) {
        EventResult eventResult = new EventResult();
        eventResult.setEventId(eventId);
        eventResult.setTeamResults(teamResults);
        eventResultRepository.save(eventResult);
    }
}
