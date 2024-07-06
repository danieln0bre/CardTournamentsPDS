package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.service.TeamService;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DefaultTeamPairingStrategy implements PairingStrategy<Team> {

    private final TeamService teamService;

    @Autowired
    public DefaultTeamPairingStrategy(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public List<Pairing> createPairings(List<Team> teams) {
        validateTeams(teams);
        teams.sort(getRankComparator());

        List<Pairing> pairings = new ArrayList<>();
        Set<String> pairedTeamIds = new HashSet<>();

        for (Team team1 : teams) {
            if (!pairedTeamIds.contains(team1.getId())) {
                Pairing pairing = createPairForTeam(team1, teams, pairedTeamIds);
                pairings.add(pairing);
                pairedTeamIds.add(team1.getId());
                if (!"Bye".equals(pairing.getEntityTwoId())) {
                    pairedTeamIds.add(pairing.getEntityTwoId());
                }
            }
        }
        return pairings;
    }

    private void validateTeams(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            throw new IllegalArgumentException("List of teams cannot be null or empty.");
        }
    }

    private Pairing createPairForTeam(Team team1, List<Team> teams, Set<String> pairedTeamIds) {
        Team team2 = findBestMatchingTeam(team1, teams, pairedTeamIds);
        if (team2 != null) {
            return new Pairing(team1.getId(), team2.getId());
        } else {
            return new Pairing(team1.getId(), "Bye");
        }
    }

    private Team findBestMatchingTeam(Team team1, List<Team> teams, Set<String> pairedTeamIds) {
        double team1AverageRankPoints = calculateAverageRankPoints(team1);
        Team bestMatch = null;
        double smallestDifference = Double.MAX_VALUE;

        for (Team team : teams) {
            if (!pairedTeamIds.contains(team.getId()) && !team1.getId().equals(team.getId())) {
                double teamAverageRankPoints = calculateAverageRankPoints(team);
                double difference = Math.abs(team1AverageRankPoints - teamAverageRankPoints);
                if (difference < smallestDifference) {
                    smallestDifference = difference;
                    bestMatch = team;
                }
            }
        }
        return bestMatch;
    }

    private double calculateAverageRankPoints(Team team) {
        return teamService.getPlayers(team).stream()
                .mapToInt(Player::getRankPoints)
                .average()
                .orElse(0.0);
    }

    private Comparator<Team> getRankComparator() {
        return Comparator.comparingInt(teamService::getEventPoints).reversed()
                         .thenComparingDouble(teamService::getWinrate);
    }
}
