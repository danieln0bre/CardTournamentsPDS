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
        List<Pairing> pairings = new ArrayList<>();
        Collections.sort(players, getRankComparator());  // Sort players based on event points (desc) and rank points

        Set<String> pairedPlayerIds = new HashSet<>();  // Track IDs of players who have been paired

        for (Player player1 : players) {
            if (!pairedPlayerIds.contains(player1.getId())) {
                // Find the matching player starting from the next index in the sorted list
                Player player2 = findMatchingPlayer(player1, players, pairedPlayerIds, players.indexOf(player1) + 1);

                if (player2 != null) {
                    // Create a new pairing
                    Pairing pairing = new Pairing(player1.getId(), player2.getId());
                    pairings.add(pairing);
                    // Update the set of paired players
                    pairedPlayerIds.add(player1.getId());
                    pairedPlayerIds.add(player2.getId());

                    // Update each player's list of opponents
                    playerService.updatePlayerOpponents(player1.getId(), player2.getId());
                    playerService.updatePlayerOpponents(player2.getId(), player1.getId());
                } else {
                    // Player gets a bye
                    Pairing byePairing = new Pairing(player1.getId(), "Bye");
                    pairings.add(byePairing);
                    pairedPlayerIds.add(player1.getId());
                    player1.addEventPoints(1);
                    playerRepository.save(player1);
                }
            }
        }

        return pairings;
    }

    private Player findMatchingPlayer(Player player, List<Player> players, Set<String> pairedPlayerIds, int startIndex) {
        if (startIndex >= players.size()) {
            return null;  // Early exit if startIndex is out of bounds
        }

        int targetEventPoints = player.getEventPoints();
        Player bestMatch = null;
        double minDifference = Double.MAX_VALUE;
        boolean foundEqualEventPoints = false;

        for (int i = startIndex; i < players.size(); i++) {
            Player potentialOpponent = players.get(i);
            // Check conditions: not the player itself, not previously opposed, not already paired in this round
            if (!potentialOpponent.equals(player) && 
                !player.getOpponentIds().contains(potentialOpponent.getId()) &&
                !pairedPlayerIds.contains(potentialOpponent.getId())) {

                double eventPointsDiff = Math.abs(potentialOpponent.getEventPoints() - targetEventPoints);
                double rankPointsDiff = Math.abs(potentialOpponent.getRankPoints() - player.getRankPoints());
                double matchWinrateDiff = Math.abs(potentialOpponent.getOpponentsMatchWinrate() - player.getOpponentsMatchWinrate());
                double score = eventPointsDiff + rankPointsDiff + matchWinrateDiff;  // Calculate combined difference

                // Choose the best match based on the criteria
                if ((eventPointsDiff == 0 && !foundEqualEventPoints) || (eventPointsDiff == 0 && score < minDifference)) {
                    foundEqualEventPoints = true;
                    minDifference = score;
                    bestMatch = potentialOpponent;
                } else if (!foundEqualEventPoints && score < minDifference) {
                    minDifference = score;
                    bestMatch = potentialOpponent;
                }
            }
        }

        return bestMatch;
    }

    private static Comparator<Player> getRankComparator() {
        return Comparator.comparing(Player::getEventPoints, Comparator.reverseOrder())
        				 .thenComparing(Player::getRankPoints);
    }
}
