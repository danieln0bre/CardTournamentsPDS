package br.ufrn.imd.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventRanking {
    public static void rankPlayers(ArrayList<Player> players) {
        Collections.sort(players, new EventPointsComparator());
    }

    public static void displayRanking(ArrayList<Player> players) {
        System.out.println("Event Ranking:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getUsername() + " - Points: " + player.getEventPoints() + 
                               ", Opponents Winrate: " + player.getOpponentsMatchWinrate() + 
                               ", Opponents Opponents Winrate: " + player.getOpponentsOpponentsMatchWinrate());
        }
    }

    static class EventPointsComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            // Compare by event points
            int compareByEventPoints = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            if (compareByEventPoints != 0) {
                return compareByEventPoints;
            }

            // If event points are equal, compare by opponents' match win rate
            int compareByOpponentsWinrate = Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
            if (compareByOpponentsWinrate != 0) {
                return compareByOpponentsWinrate;
            }

            // If opponents' match win rates are equal, compare by opponents' opponents match win rate
            return Double.compare(p2.getOpponentsOpponentsMatchWinrate(), p1.getOpponentsOpponentsMatchWinrate());
        }
    }
}
