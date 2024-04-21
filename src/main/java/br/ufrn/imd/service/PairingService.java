package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.model.Pairing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PairingService {
    
	@Autowired
	private PlayerRepository playerRepository;
	
    @Autowired
    private PlayerService playerService;

    public List<Pairing> createPairings(List<Player> players) {
        Collections.sort(players, getRankComparator()); // Use the custom comparator for sorting
        List<Pairing> pairings = new ArrayList<>();
        Set<String> pairedPlayerIds = new HashSet<>();

        for (Player player1 : players) {
            if (!pairedPlayerIds.contains(player1.getId())) {
                Player player2 = findMatchingPlayer(player1, players, pairedPlayerIds, players.indexOf(player1) + 1);

                if (player2 != null) {
                    Pairing pairing = new Pairing(player1.getId(), player2.getId());
                    pairings.add(pairing);
                    pairedPlayerIds.add(player1.getId());
                    pairedPlayerIds.add(player2.getId());
                } else {
                    // Player 1 gets a bye
                    Pairing byePairing = new Pairing(player1.getId(), "Bye");
                    pairings.add(byePairing);
                    pairedPlayerIds.add(player1.getId());
                    updatePlayerForBye(player1.getId());  // Assign points for Bye match
                }
            }
        }

        return pairings;
    }

    private Player findMatchingPlayer(Player player, List<Player> players, Set<String> pairedPlayerIds, int startIndex) {
        if (startIndex >= players.size()) {
            return null;  // Early exit if startIndex is out of bounds
        }

        double minDifference = Double.MAX_VALUE;
        Player bestMatch = null;

        for (int i = startIndex; i < players.size(); i++) {
            Player potentialOpponent = players.get(i);
            if (!potentialOpponent.equals(player) && 
                !player.getOpponentIds().contains(potentialOpponent.getId()) &&
                !pairedPlayerIds.contains(potentialOpponent.getId())) {

                double score = calculateMatchScore(player, potentialOpponent);

                if (score < minDifference) {
                    minDifference = score;
                    bestMatch = potentialOpponent;
                }
            }
        }
        return bestMatch;
    }

    private double calculateMatchScore(Player player, Player opponent) {
        double eventPointsDiff = Math.abs(opponent.getEventPoints() - player.getEventPoints());
        double rankPointsDiff = Math.abs(opponent.getRankPoints() - player.getRankPoints());
        double matchWinrateDiff = Math.abs(opponent.getOpponentsMatchWinrate() - player.getOpponentsMatchWinrate());
        return eventPointsDiff + rankPointsDiff + matchWinrateDiff;  // Combined difference as a score
    }

    private static Comparator<Player> getRankComparator() {
        return Comparator.comparing(Player::getEventPoints, Comparator.reverseOrder())
                         .thenComparing(Player::getRankPoints);
    }

    private void updatePlayerForBye(String playerId) {
        playerRepository.findById(playerId).ifPresent(player -> {
            player.addEventPoints(3);  // Assuming 3 points for a win
            playerRepository.save(player);
        });
    }
}
