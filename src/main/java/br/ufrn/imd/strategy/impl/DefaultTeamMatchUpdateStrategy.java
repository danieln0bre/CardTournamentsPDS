package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.repository.TeamRepository;
import br.ufrn.imd.service.TeamService;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultTeamMatchUpdateStrategy implements MatchUpdateStrategy {

    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private Map<String, Map<String, Map<String, Integer[]>>> eventTeamMatchups;

    @Autowired
    public DefaultTeamMatchUpdateStrategy(TeamRepository teamRepository, TeamService teamService) {
        this.teamRepository = teamRepository;
        this.teamService = teamService;
        this.eventTeamMatchups = new HashMap<>();
    }

    @Override
    public void updateMatchResult(Pairing pairing) {
        validatePairing(pairing);
        handleByeMatch(pairing);
        updateTeamResults(pairing);
    }

    @Override
    public void validatePairing(Pairing pairing) {
        if (pairing == null) {
            throw new IllegalArgumentException("Pairing cannot be null.");
        }
        if (pairing.getResult() < 0 || pairing.getResult() > 1) {
            throw new IllegalArgumentException("Invalid match result. Must be 0 or 1.");
        }
    }

    @Override
    public void handleByeMatch(Pairing pairing) {
        if ("Bye".equals(pairing.getEntityOneId())) {
            updateTeamForBye(pairing.getEntityTwoId());
        } else if ("Bye".equals(pairing.getEntityTwoId())) {
            updateTeamForBye(pairing.getEntityOneId());
        }
    }

    @Override
    public void updatePlayersResults(Pairing pairing) {
        throw new UnsupportedOperationException("updatePlayersResults not supported in DefaultTeamMatchUpdateStrategy.");
    }

    public void updateTeamResults(Pairing pairing) {
        if ("Bye".equals(pairing.getEntityOneId()) || "Bye".equals(pairing.getEntityTwoId())) {
            return;
        }

        Team teamOne = fetchTeam(pairing.getEntityOneId());
        Team teamTwo = fetchTeam(pairing.getEntityTwoId());

        if (pairing.getResult() == 0) {
            teamOne.setEventPoints(teamOne.getEventPoints() + 1); // Vitória do time 1
            teamTwo.setEventPoints(teamTwo.getEventPoints() + 0); // Derrota do time 2
        } else if (pairing.getResult() == 1) {
            teamTwo.setEventPoints(teamTwo.getEventPoints() + 1); // Vitória do time 2
            teamOne.setEventPoints(teamOne.getEventPoints() + 0); // Derrota do time 1
        }

        updateWinrate(teamOne);
        updateWinrate(teamTwo);

        teamRepository.save(teamOne);
        teamRepository.save(teamTwo);
    }

    private void updateWinrate(Team team) {
        List<String> opponentTeamIds = team.getOpponentTeamIds();
        int numberOfMatches = opponentTeamIds != null ? opponentTeamIds.size() : 0;
        if (numberOfMatches > 0) {
            double winrate = (double) team.getEventPoints() / numberOfMatches;
            team.setWinrate((int) winrate);
        } else {
            team.setWinrate(0);
        }
    }

    @Override
    public void updateGameObjectMatchups(String eventId, List<Pairing> pairings) {
        Map<String, Map<String, Integer[]>> teamMatchups = eventTeamMatchups.getOrDefault(eventId, new HashMap<>());

        for (Pairing pairing : pairings) {
            String teamOneId = pairing.getEntityOneId();
            String teamTwoId = pairing.getEntityTwoId();

            Team teamOne = teamRepository.findById(teamOneId).orElse(null);
            Team teamTwo = teamRepository.findById(teamTwoId).orElse(null);

            if (teamOne == null || teamTwo == null) continue;

            teamMatchups.putIfAbsent(teamOneId, new HashMap<>());
            teamMatchups.putIfAbsent(teamTwoId, new HashMap<>());

            teamMatchups.get(teamOneId).putIfAbsent(teamTwoId, new Integer[]{0, 0});
            teamMatchups.get(teamTwoId).putIfAbsent(teamOneId, new Integer[]{0, 0});

            Integer[] resultsTeamOne = teamMatchups.get(teamOneId).get(teamTwoId);
            Integer[] resultsTeamTwo = teamMatchups.get(teamTwoId).get(teamOneId);

            if (pairing.getResult() == 0) {
                resultsTeamOne[0]++;
            } else if (pairing.getResult() == 1) {
                resultsTeamTwo[0]++;
            }

            resultsTeamOne[1]++;
            resultsTeamTwo[1]++;

            teamMatchups.get(teamOneId).put(teamTwoId, resultsTeamOne);
            teamMatchups.get(teamTwoId).put(teamOneId, resultsTeamTwo);
        }

        eventTeamMatchups.put(eventId, teamMatchups);
    }

    private Team fetchTeam(String teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
    }

    private void updateTeamForBye(String teamId) {
        Team team = fetchTeam(teamId);
        team.setEventPoints(team.getEventPoints() + 1); // Vitória automática para Bye
        updateWinrate(team);
        teamRepository.save(team);
    }
}
