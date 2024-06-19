package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.strategy.EventRankingStrategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class DefaultEventRankingStrategy implements EventRankingStrategy {

    @Override
    public List<Player> rankPlayers(List<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("List of players cannot be null.");
        }
        List<Player> sortedPlayers = players.stream().sorted(new EventPointsAndOpponentMatchWinrateComparator()).collect(Collectors.toList());
        return sortedPlayers;
    }

    @Override
    public List<PlayerResult> rankPlayerResults(List<PlayerResult> playerResults) {
        return playerResults.stream()
                .sorted(Comparator.comparingInt(PlayerResult::getEventPoints).reversed())
                .collect(Collectors.toList());
    }

    private static class EventPointsAndOpponentMatchWinrateComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            int eventPointsComparison = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            if (eventPointsComparison != 0) {
                return eventPointsComparison;
            }
            return Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
        }
    }
}
