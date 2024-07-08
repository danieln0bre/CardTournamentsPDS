package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Team;
import br.ufrn.imd.model.TeamResult;
import br.ufrn.imd.service.TeamService;
import br.ufrn.imd.strategy.EventRankingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("defaultTeamEventRankingStrategy")
public class DefaultTeamEventRankingStrategy implements EventRankingStrategy<Team, TeamResult> {

    private final TeamService teamService;

    @Autowired
    public DefaultTeamEventRankingStrategy(TeamService teamService) {
        this.teamService = teamService;
    }

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

    private class TeamPointsAndWinrateComparator implements Comparator<Team> {
        @Override
        public int compare(Team t1, Team t2) {
            int eventPointsComparison = Integer.compare(t2.getEventPoints(), t1.getEventPoints());
            if (eventPointsComparison != 0) {
                return eventPointsComparison;
            }
            return Double.compare(t2.getEventPoints(), t1.getEventPoints());
        }
    }
}
