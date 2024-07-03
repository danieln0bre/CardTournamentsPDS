package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Team;
import br.ufrn.imd.model.TeamResult;
import br.ufrn.imd.strategy.EventRankingStrategy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class DefaultTeamEventRankingStrategy implements EventRankingStrategy<Team, TeamResult> {

    @Override
    public List<Team> rankEntities(List<Team> teams) {
        if (teams == null) {
            throw new IllegalArgumentException("List of teams cannot be null.");
        }
        return teams.stream().sorted(new TeamPointsAndWinrateComparator()).collect(Collectors.toList());
    }

    @Override
    public List<TeamResult> rankEntityResults(List<TeamResult> teamResults) {
        return teamResults.stream()
                .sorted(Comparator.comparingInt(TeamResult::getEventPoints).reversed())
                .collect(Collectors.toList());
    }

    private static class TeamPointsAndWinrateComparator implements Comparator<Team> {
        @Override
        public int compare(Team t1, Team t2) {
            int eventPointsComparison = Integer.compare(t2.getEventPoints(), t1.getEventPoints());
            if (eventPointsComparison != 0) {
                return eventPointsComparison;
            }
            return Double.compare(t2.getWinrate(), t1.getWinrate());
        }
    }

    public List<Team> sortByEventPoints(List<Team> teams) {
        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                int eventPointsComparison = Integer.compare(t2.getEventPoints(), t1.getEventPoints());
                if (eventPointsComparison != 0) {
                    return eventPointsComparison;
                }
                return Double.compare(t2.getWinrate(), t1.getWinrate());
            }
        });
        return teams;
    }
}
