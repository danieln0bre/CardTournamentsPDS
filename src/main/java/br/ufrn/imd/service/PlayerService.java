package br.ufrn.imd.service;

import java.util.Comparator;

import br.ufrn.imd.model.Player;

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


    public static void calculateWinRates(Player player) {
        player.calculateOpponentsMatchWinrate();
        player.calculateOpponentsOpponentsMatchWinrate();
    }

}
