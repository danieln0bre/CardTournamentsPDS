package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class PlayerService {
    public static Comparator<Player> getRankComparator() {
        return new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                int compareByEventPoints = Integer.compare(p2.getEventPoints(), p1.getEventPoints()); // Sort in descending order of event points
                if (compareByEventPoints != 0) {
                    return compareByEventPoints;
                }
                int compareByRankPoints = Integer.compare(p1.getRankPoints(), p2.getRankPoints());
                if (compareByRankPoints != 0) {
                    return compareByRankPoints;
                }
                // Add additional comparisons if needed
                return 0; // Default comparison
            }
        };
    }

    public static void calculateOpponentsMatchWinrate(Player player) {
        double totalOpponentsWins = 0;
        int totalOpponentsMatches = player.getOpponents().size();

        for (Player opponent : player.getOpponents()) {
            if (opponent.getEventPoints() > 0) { // Assuming event points represent wins
                totalOpponentsWins++;
            }
        }

        double winRate = calculateWinrate(totalOpponentsWins, totalOpponentsMatches);
        player.setOpponentsMatchWinrate(winRate);
    }

    public static void calculateWinRates(Player player) {
        calculateOpponentsMatchWinrate(player);
    }

    // Utility method to calculate winrate
    private static double calculateWinrate(double wins, int totalMatches) {
        if (totalMatches > 0) {
            return wins / totalMatches;
        } else {
            return 0.0;
        }
    }
}
