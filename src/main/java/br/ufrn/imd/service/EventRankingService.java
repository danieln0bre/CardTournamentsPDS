package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventRankingService {
    public static ArrayList<Player> sortByEventPoints(ArrayList<Player> players) {
        // Create a copy of the original list to avoid modifying the input list
        ArrayList<Player> sortedPlayers = new ArrayList<>(players);

        // Sort the players by event points and opponent match win rate
        Collections.sort(sortedPlayers, new EventPointsAndOpponentMatchWinrateComparator());
        
        return sortedPlayers;
    }

    // Comparator for sorting players based on event points and opponent match win rate
    static class EventPointsAndOpponentMatchWinrateComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            // First, compare by event points (descending order)
            int eventPointsComparison = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            if (eventPointsComparison != 0) {
                return eventPointsComparison;
            }
            
            // If event points are equal, compare by opponent match win rate (descending order)
            return Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
        }
    }
}
