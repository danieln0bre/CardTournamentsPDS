package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class PlayerService {
    public static Comparator<Player> getRankComparator() {
        return (p1, p2) -> {
            int compareByEventPoints = Integer.compare(p2.getEventPoints(), p1.getEventPoints()); // Sort in descending order of event points
            if (compareByEventPoints != 0) {
                return compareByEventPoints;
            }
            return Integer.compare(p1.getRankPoints(), p2.getRankPoints());
        };
    }
    
    private static void calculateOpponentsMatchWinrate(Player player, ArrayList<Player> players) {
        int totalOpponents = player.getOpponentIds().size();
        double totalOpponentWinrate = 0;

        System.out.println("Calculating opponents match winrate for player: " + player.getUsername());

        for (Long opponentId : player.getOpponentIds()) {
            Player opponent = getPlayerById(players, opponentId);
            if (opponent != null && opponent.getEventPoints() > 0 && totalOpponents > 0) {
                // Calculate winrate for the opponent
                double opponentWinrate = opponent.getEventPoints() / (double) totalOpponents;
                System.out.println("Winrate for opponent " + opponent.getUsername() + ": " + opponentWinrate);
                totalOpponentWinrate += opponentWinrate; // Accumulate winrate
            }
        }

        // Calculate mean value
        double meanOpponentWinrate = totalOpponentWinrate / totalOpponents;
        System.out.println("Mean opponent winrate for player " + player.getUsername() + ": " + meanOpponentWinrate);

        // Set mean value as OpponentsMatchWinrate
        player.setOpponentsMatchWinrate(meanOpponentWinrate);
    }


    public static void calculateWinRates(Player player, ArrayList<Player> players) {
        System.out.println("Calculating win rates for player: " + player.getUsername());
        calculateOpponentsMatchWinrate(player, players);
        System.out.println("Win rates calculated for player: " + player.getUsername());
    }



    // Method to get player by ID
    private static Player getPlayerById(ArrayList<Player> players, long playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null; // Player not found
    }

    // Utility method to calculate winrate
    private static double calculateWinrate(double wins, int totalMatches) {
        if (totalMatches > 0) {
            return wins / totalMatches;
        } else {
            return 0.0; // Handle division by zero gracefully
        }
    }
}
