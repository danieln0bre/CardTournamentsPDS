package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.TeamResult;
import br.ufrn.imd.strategy.StatisticsGenerationStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultTeamStatisticsGenerationStrategy implements StatisticsGenerationStrategy {

    @Override
    public Map<String, Map<String, Double>> generateStatistics(EventResult eventResult) {
        Map<String, Map<String, Integer[]>> deckMatchups = new HashMap<>();

        for (TeamResult teamResult : eventResult.getTeamResults()) {
            String teamId = teamResult.getTeamId();
            for (String opponentId : teamResult.getOpponentTeamIds()) {
                String opponentDeckId = getOpponentTeamId(eventResult.getTeamResults(), opponentId);
                if (opponentDeckId != null) {
                    deckMatchups.putIfAbsent(teamId, new HashMap<>());
                    deckMatchups.get(teamId).putIfAbsent(opponentDeckId, new Integer[]{0, 0});
                    deckMatchups.get(teamId).get(opponentDeckId)[1]++;
                    if (teamResult.getWinrate() > 0.5) {
                        deckMatchups.get(teamId).get(opponentDeckId)[0]++;
                    }
                }
            }
        }

        Map<String, Map<String, Double>> winPercentageMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer[]>> entry : deckMatchups.entrySet()) {
            String teamId = entry.getKey();
            Map<String, Double> opponentWinPercentages = new HashMap<>();
            for (Map.Entry<String, Integer[]> opponentEntry : entry.getValue().entrySet()) {
                String opponentDeckId = opponentEntry.getKey();
                Integer[] results = opponentEntry.getValue();
                if (results[1] != 0) {
                    double winPercentage = (double) results[0] / results[1] * 100;
                    opponentWinPercentages.put(opponentDeckId, winPercentage);
                }
            }
            winPercentageMap.put(teamId, opponentWinPercentages);
        }
        return winPercentageMap;
    }

    private String getOpponentTeamId(List<TeamResult> teamResults, String opponentId) {
        for (TeamResult teamResult : teamResults) {
            if (teamResult.getTeamId().equals(opponentId)) {
                return teamResult.getTeamId();
            }
        }
        return null;
    }
}
