package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
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
    private final Map<String, Map<String, Integer[]>> gameObjectMatchups = new HashMap<>();

    @Autowired
    public DefaultTeamMatchUpdateStrategy(TeamRepository teamRepository, TeamService teamService) {
        this.teamRepository = teamRepository;
        this.teamService = teamService;
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
        if ("Bye".equals(pairing.getEntityTwoId())) {
            updateTeamForBye(pairing.getEntityOneId());
        } else if ("Bye".equals(pairing.getEntityOneId())) {
            updateTeamForBye(pairing.getEntityTwoId());
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
            teamOne.setEventPoints(teamService.getEventPoints(teamOne) + 1);
        } else if (pairing.getResult() == 1) {
            teamTwo.setEventPoints(teamService.getEventPoints(teamTwo) + 1);
        }

        teamRepository.save(teamOne);
        teamRepository.save(teamTwo);
    }

    @Override
    public void updateGameObjectMatchups(String eventId, List<Pairing> pairings) {
        Map<String, Map<String, Integer[]>> gameObjectMatchups = new HashMap<>();

        for (Pairing pairing : pairings) {
            String teamOneId = pairing.getEntityOneId();
            String teamTwoId = pairing.getEntityTwoId();

            Team teamOne = teamRepository.findById(teamOneId).orElse(null);
            Team teamTwo = teamRepository.findById(teamTwoId).orElse(null);

            if (teamOne == null || teamTwo == null) continue;

            for (Player playerOne : teamService.getPlayers(teamOne)) {
                for (Player playerTwo : teamService.getPlayers(teamTwo)) {
                    String playerOneGameObjectId = playerOne.getGameObjectId();
                    String playerTwoGameObjectId = playerTwo.getGameObjectId();

                    gameObjectMatchups.putIfAbsent(playerOneGameObjectId, new HashMap<>());
                    gameObjectMatchups.putIfAbsent(playerTwoGameObjectId, new HashMap<>());

                    gameObjectMatchups.get(playerOneGameObjectId).putIfAbsent(playerTwoGameObjectId, new Integer[]{0, 0});
                    gameObjectMatchups.get(playerTwoGameObjectId).putIfAbsent(playerOneGameObjectId, new Integer[]{0, 0});

                    Integer[] resultsPlayerOne = gameObjectMatchups.get(playerOneGameObjectId).get(playerTwoGameObjectId);
                    Integer[] resultsPlayerTwo = gameObjectMatchups.get(playerTwoGameObjectId).get(playerOneGameObjectId);

                    if (pairing.getResult() == 0) {
                        resultsPlayerOne[0]++;
                    } else if (pairing.getResult() == 1) {
                        resultsPlayerTwo[0]++;
                    }

                    resultsPlayerOne[1]++;
                    resultsPlayerTwo[1]++;

                    gameObjectMatchups.get(playerOneGameObjectId).put(playerTwoGameObjectId, resultsPlayerOne);
                    gameObjectMatchups.get(playerTwoGameObjectId).put(playerOneGameObjectId, resultsPlayerTwo);
                }
            }
        }

        // Save or update the matchups in the repository or other storage if needed.
    }

    private Team fetchTeam(String teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
    }

    private void updateTeamForBye(String teamId) {
        Team team = fetchTeam(teamId);
        team.setEventPoints(teamService.getEventPoints(team) + 1);
        teamRepository.save(team);
    }
}
