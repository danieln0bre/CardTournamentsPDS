package br.ufrn.imd;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.service.PairingService;
import br.ufrn.imd.service.PlayerService;
import br.ufrn.imd.service.RankingService;
import br.ufrn.imd.model.EventRanking;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Create players
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "Player1", "password1"));
        players.add(new Player(2, "Player2", "password2"));
        players.add(new Player(3, "Player3", "password3"));
        players.add(new Player(4, "Player4", "password4"));

        // Set rank points and event points for players
        players.get(0).setRankPoints(210);
        players.get(0).setEventPoints(3);

        players.get(1).setRankPoints(180);
        players.get(1).setEventPoints(3);

        players.get(2).setRankPoints(150);
        players.get(2).setEventPoints(0);

        players.get(3).setRankPoints(168);
        players.get(3).setEventPoints(0);

        // Adding opponents for each player (for testing purposes)
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                players.get(i).addOpponent(players.get(j));
                players.get(j).addOpponent(players.get(i));
            }
        }

        // Calculate win rates for players
        for (Player player : players) {
            PlayerService.calculateWinRates(player);
        }

        // Rank players based on event points
        EventRanking.sortByEventPoints(players);

        // Display event ranking
        GeneralRankingService.displayRanking(players);

        // Create pairings
        ArrayList<Pairing> pairings = PairingService.createPairings(players);
    }
}
