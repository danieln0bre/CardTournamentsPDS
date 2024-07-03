package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.strategy.EventRankingStrategy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultPlayerEventRankingStrategy implements EventRankingStrategy<Player, PlayerResult> {

    @Override
    public List<Player> rankEntities(List<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("List of players cannot be null.");
        }
        return players.stream().sorted(new PlayerPointsAndWinrateComparator()).collect(Collectors.toList());
    }

    @Override
    public List<PlayerResult> rankEntityResults(List<PlayerResult> playerResults) {
        return playerResults.stream()
                .sorted(Comparator.comparingInt(PlayerResult::getEventPoints).reversed())
                .collect(Collectors.toList());
    }

    private static class PlayerPointsAndWinrateComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            int eventPointsComparison = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            if (eventPointsComparison != 0) {
                return eventPointsComparison;
            }
            return Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
        }
    }

    public List<Player> sortByEventPoints(List<Player> players) {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                int eventPointsComparison = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
                if (eventPointsComparison != 0) {
                    return eventPointsComparison;
                }
                return Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
            }
        });
        return players;
    }
}
