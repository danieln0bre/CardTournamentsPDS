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
        Map<String, Map<String, Integer[]>> teamMatchups = new HashMap<>();

        for (TeamResult teamResult : eventResult.getTeamResults()) {
            String teamId = teamResult.getTeamId();
            System.out.println("Processando resultado para o time com ID: " + teamId);

            if (teamResult.getOpponentTeamIds() == null) {
                System.out.println("A lista de IDs dos times oponentes é nula para o time com ID: " + teamId);
                continue;
            }

            for (String opponentId : teamResult.getOpponentTeamIds()) {
                System.out.println("ID do oponente: " + opponentId);
                String opponentTeamId = getOpponentTeamId(eventResult.getTeamResults(), opponentId);
                if (opponentTeamId != null) {
                    System.out.println("ID do oponente encontrado: " + opponentTeamId);
                    teamMatchups.putIfAbsent(teamId, new HashMap<>());
                    teamMatchups.get(teamId).putIfAbsent(opponentTeamId, new Integer[]{0, 0});
                    teamMatchups.get(teamId).get(opponentTeamId)[1]++;

                    int teamPoints = teamResult.getEventPoints();
                    int opponentPoints = getOpponentTeamEventPoints(eventResult.getTeamResults(), opponentId);
                    System.out.println("Pontos do time: " + teamPoints + ", Pontos do oponente: " + opponentPoints);

                    if (teamPoints > opponentPoints) {
                        teamMatchups.get(teamId).get(opponentTeamId)[0]++;
                        System.out.println("Incrementando contagem de vitórias para o time com ID: " + teamId + " contra oponente com ID: " + opponentTeamId);
                    }
                } else {
                    System.out.println("Oponente não encontrado para o ID: " + opponentId);
                }
            }
        }


        Map<String, Map<String, Double>> winPercentageMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer[]>> entry : teamMatchups.entrySet()) {
            String teamId = entry.getKey();
            Map<String, Double> opponentWinPercentages = new HashMap<>();
            for (Map.Entry<String, Integer[]> opponentEntry : entry.getValue().entrySet()) {
                String opponentTeamId = opponentEntry.getKey();
                Integer[] results = opponentEntry.getValue();
                if (results[1] != 0) {
                    double winPercentage = (double) results[0] / results[1] * 100;
                    opponentWinPercentages.put(opponentTeamId, winPercentage);
                }
            }
            winPercentageMap.put(teamId, opponentWinPercentages);
        }
        System.out.println(winPercentageMap);
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

    private int getOpponentTeamEventPoints(List<TeamResult> teamResults, String opponentId) {
        for (TeamResult teamResult : teamResults) {
            if (teamResult.getTeamId().equals(opponentId)) {
                return teamResult.getEventPoints();
            }
        }
        return 0;
    }
}
