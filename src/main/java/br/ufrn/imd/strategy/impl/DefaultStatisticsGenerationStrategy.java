package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.strategy.StatisticsGenerationStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultStatisticsGenerationStrategy implements StatisticsGenerationStrategy {

    @Override
    public Map<String, Map<String, Double>> generateStatistics(EventResult eventResult) {
        Map<String, Map<String, Integer[]>> deckMatchups = new HashMap<>();
        
        for (PlayerResult playerResult : eventResult.getPlayerResults()) {
            String deckId = playerResult.getDeckId();
            for (String opponentId : playerResult.getOpponentIds()) {
                String opponentDeckId = getOpponentDeckId(eventResult.getPlayerResults(), opponentId);
                if (opponentDeckId != null) {
                    deckMatchups.putIfAbsent(deckId, new HashMap<>());
                    deckMatchups.get(deckId).putIfAbsent(opponentDeckId, new Integer[]{0, 0});
                    deckMatchups.get(deckId).get(opponentDeckId)[1]++;
                    if (playerResult.getWinrate() > 0.5) {
                        deckMatchups.get(deckId).get(opponentDeckId)[0]++;
                    }
                }
            }
        }

        Map<String, Map<String, Double>> winPercentageMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer[]>> entry : deckMatchups.entrySet()) {
            String deckId = entry.getKey();
            Map<String, Double> opponentWinPercentages = new HashMap<>();
            for (Map.Entry<String, Integer[]> opponentEntry : entry.getValue().entrySet()) {
                String opponentDeckId = opponentEntry.getKey();
                Integer[] results = opponentEntry.getValue();
                if (results[1] != 0) {
                    double winPercentage = (double) results[0] / results[1] * 100;
                    opponentWinPercentages.put(opponentDeckId, winPercentage);
                }
            }
            winPercentageMap.put(deckId, opponentWinPercentages);
        }
        return winPercentageMap;
    }

    private String getOpponentDeckId(List<PlayerResult> playerResults, String opponentId) {
        for (PlayerResult playerResult : playerResults) {
            if (playerResult.getPlayerId().equals(opponentId)) {
                return playerResult.getDeckId();
            }
        }
        return null;
    }
}
