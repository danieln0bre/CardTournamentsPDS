package br.ufrn.imd.service;

import java.util.ArrayList;

import br.ufrn.imd.model.Player;

public class RankingService {
    // Display the ranking of players
    public static void displayRanking(ArrayList<Player> players) {
        System.out.println("Ranking:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            displayPlayerRank(player, i + 1);
        }
    }

    // Display individual player's rank and relevant information
    private static void displayPlayerRank(Player player, int rank) {
        System.out.println(rank + ". " + player.getUsername() + 
                           " - Points: " + player.getEventPoints() + 
                           ", Opponents Winrate: " + player.getOpponentsMatchWinrate() + 
                           ", Opponents Opponents Winrate: " + player.getOpponentsOpponentsMatchWinrate());
    }
}
