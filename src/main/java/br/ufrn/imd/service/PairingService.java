package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Pairing;

import java.util.ArrayList;
import java.util.Collections;

public class PairingService {
	public static ArrayList<Pairing> createPairings(ArrayList<Player> players) {
	    ArrayList<Pairing> pairings = new ArrayList<>();

	    // Sorting players based on rankPoints, eventPoints, opponentsMatchWinrate, and opponentsOpponentsMatchWinrate
	    Collections.sort(players, PlayerService.getRankComparator());

	    // Creating pairings
	    for (int i = 0; i < players.size(); i += 2) {
	        if (i + 1 < players.size()) {
	            Player player1 = players.get(i);
	            Player player2 = findMatchingPlayer(player1, players, i + 1);
	            if (player1 == null || player2 == null) {
	                continue; // Skip this iteration and move to the next one
	            }
	            Pairing pairing = new Pairing(player1, player2);
	            pairings.add(pairing);
	        }
	    }

	    return pairings;
	}


	private static Player findMatchingPlayer(Player player, ArrayList<Player> players, int startIndex) {
	    int targetEventPoints = player.getEventPoints();
	    Player bestMatch = null;
	    double minDifference = Double.MAX_VALUE;

	    for (int i = startIndex; i < players.size(); i++) {
	        Player opponent = players.get(i);
	        if (opponent.getEventPoints() == targetEventPoints && !opponent.equals(player)) {
	            // Calculate the difference in rankPoints, opponentsMatchWinrate, and opponentsOpponentsMatchWinrate
	            double rankPointsDiff = Math.abs(opponent.getRankPoints() - player.getRankPoints());
	            double matchWinrateDiff = Math.abs(opponent.getOpponentsMatchWinrate() - player.getOpponentsMatchWinrate());

	            // Calculate a score based on the differences
	            double score = rankPointsDiff + matchWinrateDiff;

	            // Update the best match if this opponent has a smaller score
	            if (score < minDifference) {
	                minDifference = score;
	                bestMatch = opponent;
	            }
	        }
	    }
	    return bestMatch;
	}
}
