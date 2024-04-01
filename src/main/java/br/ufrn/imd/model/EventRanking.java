package br.ufrn.imd.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventRanking {
	
	// ISSO NAO EH CLASSE MODEL E SIM SERVICE...
	
	// Sort players by event points, opponents' match win rate, and opponents' opponents match win rate
    public static void sortByEventPoints(ArrayList<Player> players) {
        Collections.sort(players, new EventPointsComparator());
    }

    // Comparator for sorting players based on event points, opponents' match win rate, and opponents' opponents match win rate
    static class EventPointsComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            int compareByEventPoints = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            if (compareByEventPoints != 0) {
                return compareByEventPoints;
            }

            int compareByOpponentsWinrate = Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
            if (compareByOpponentsWinrate != 0) {
                return compareByOpponentsWinrate;
            }

            return Double.compare(p2.getOpponentsOpponentsMatchWinrate(), p1.getOpponentsOpponentsMatchWinrate());
        }
    }

}
