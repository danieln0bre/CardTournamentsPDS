package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.repository.PlayerRepository;
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

    public List<Pairing> createPairings(ArrayList<Player> players) {
        List<Pairing> pairings = new ArrayList<>();
        Collections.sort(players, getRankComparator());
        Set<Player> pairedPlayers = new HashSet<>();

        for (Player player1 : players) {
            if (!pairedPlayers.contains(player1)) {
                Player player2 = findMatchingPlayer(player1, players, players.indexOf(player1) + 1);

                if (player2 != null) {
                    Pairing pairing = new Pairing(player1, player2);
                    pairings.add(pairing);
                    pairedPlayers.add(player1);
                    pairedPlayers.add(player2);

                    // Update opponentIds for both players involved in the pairing
                    updatePlayerOpponents(player1, player2.getId());
                    updatePlayerOpponents(player2, player1.getId());
                }
            }
        }

        return pairings;
    }

    private void updatePlayerOpponents(Player player, String opponentId) {
        player.getOpponentIds().add(opponentId);
        playerRepository.save(player);  // Save each player after updating their opponent IDs
    }

    private Player findMatchingPlayer(Player player, List<Player> players, int startIndex) {
        if (startIndex >= players.size()) return null;  // Early exit if startIndex is out of bounds

        int targetEventPoints = player.getEventPoints();
        Player bestMatch = null;
        double minDifference = Double.MAX_VALUE;
        boolean foundEqualEventPoints = false;

        for (int i = startIndex; i < players.size(); i++) {
            Player opponent = players.get(i);
            if (!opponent.equals(player) && !opponent.getOpponentIds().contains(player.getId())) {
                double eventPointsDiff = Math.abs(opponent.getEventPoints() - targetEventPoints);
                double rankPointsDiff = Math.abs(opponent.getRankPoints() - player.getRankPoints());
                double matchWinrateDiff = Math.abs(opponent.getOpponentsMatchWinrate() - player.getOpponentsMatchWinrate());
                double score = eventPointsDiff + rankPointsDiff + matchWinrateDiff;  // Consider using a weighted score

                if ((eventPointsDiff == 0 && !foundEqualEventPoints) || (eventPointsDiff == 0 && score < minDifference)) {
                    foundEqualEventPoints = true;
                    minDifference = score;
                    bestMatch = opponent;
                } else if (!foundEqualEventPoints && score < minDifference) {
                    minDifference = score;
                    bestMatch = opponent;
                }
            }
        }

        return bestMatch;
    }

    private static Comparator<Player> getRankComparator() {
        return Comparator
            .comparing(Player::getEventPoints, Comparator.reverseOrder())
            .thenComparing(Player::getRankPoints);
    }
}
