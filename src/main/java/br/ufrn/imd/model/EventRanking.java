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
            System.out.println((i + 1) + ". " + player.getUsername() + " - Points: " + player.getEventPoints());
        }
    }

    static class EventPointsComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            // Sort in descending order of event points
            int compareByEventPoints = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            if (compareByEventPoints != 0) {
                return compareByEventPoints;
            }
            // If event points are equal, secondary sorting criteria can be applied here
            // For example, if you want to sort by rank points as a secondary criteria:
            // return Integer.compare(p2.getRankPoints(), p1.getRankPoints());
            return 0; // Default comparison
        }
    }
}
