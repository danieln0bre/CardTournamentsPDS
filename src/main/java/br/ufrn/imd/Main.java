//package br.ufrn.imd;
//
//import br.ufrn.imd.model.Pairing;
//import br.ufrn.imd.model.Player;
//import br.ufrn.imd.service.EventRankingService;
//import br.ufrn.imd.service.GeneralRankingService;
//import br.ufrn.imd.service.PairingService;
//import br.ufrn.imd.service.PlayerService;
//
//import java.util.ArrayList;
//
//public class Main {
//    public static void main(String[] args) {
//        // Get mocked player data
//        ArrayList<Player> players = getMockedPlayerData();
//
//        // Simulate 10 rounds
//        for (int round = 1; round <= 10; round++) {
//            // Create pairings for this round
//            ArrayList<Pairing> pairings = PairingService.createPairings(players);
//            
//            // Simulate matches and update event points
//            simulateMatches(pairings);
//        }
//
//        // Rank players based on event points
//        EventRankingService.sortByEventPoints(players);
//
//        // Display event ranking
//        GeneralRankingService.displayRanking(players);
//    }
//
//    public static ArrayList<Player> getMockedPlayerData() {
//        // Create players
//        ArrayList<Player> players = new ArrayList<>();
//        
//        // Add the first two players with 0 points
//        for (int i = 1; i <= 2; i++) {
//            Player player = new Player((str)i, "Player" + i, "password" + i);
//            player.setEventPoints(0); // Initialize event points to 0
//            player.setRankPoints(400 - (i * 10)); // Decreasing rank points for each player
//            players.add(player);
//        }
//        
//        // Add the rest of the players with event points in ascending order
//        for (int i = 3; i <= 12; i++) {
//            Player player = new Player(i, "Player" + i, "password" + i);
//            player.setEventPoints(i - 2); // Event points start from 1 for the third player and increment by 1 for each subsequent player
//            player.setRankPoints(400 - (i * 10)); // Decreasing rank points for each player
//            players.add(player);
//        }
//
//        // Add opponents for each player
//        for (int i = 0; i < players.size(); i++) {
//            Player currentPlayer = players.get(i);
//            for (int j = i + 1; j < players.size(); j++) {
//                Player opponent = players.get(j);
//                currentPlayer.addOpponentId(opponent.getId());
//                opponent.addOpponentId(currentPlayer.getId());
//            }
//        }
//        
//        for (Player player : players) {
//            PlayerService.calculateWinRates(player, players);
//        }
//
//        return players;
//    }
//
//
//    public static void simulateMatches(ArrayList<Pairing> pairings) {
//        // Simulate matches based on pairings
//        for (Pairing pairing : pairings) {
//            Player player1 = pairing.getPlayer1();
//            Player player2 = pairing.getPlayer2();
//
//            // Simulate match outcome (for demonstration purpose, just increment player 1's event points)
//            player1.setEventPoints(player1.getEventPoints() + 1);
//        }
//    }
//}
