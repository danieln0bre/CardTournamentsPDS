package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;

import java.util.ArrayList;

public class GeneralRankingService {
    public static void displayRanking(ArrayList<Player> players) {
        System.out.println("Ranking:");
        int rank = 1;
        for (Player player : players) {
            System.out.println(rank + ". " + player.getUsername() + " - Points: " + player.getEventPoints() + ", Opponents Winrate: " + player.getOpponentsMatchWinrate());
            rank++;
        }
    }
}
