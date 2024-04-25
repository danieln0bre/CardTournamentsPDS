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

    public List<Pairing> createPairings(List<Player> players) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("List of players cannot be null or empty.");
        }

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
                    // Jogador 1 é pareado com "Bye"
                    Pairing byePairing = new Pairing(player1.getId(), "Bye");
                    pairings.add(byePairing);
                    pairedPlayerIds.add(player1.getId());
                    updatePlayerForBye(player1.getId());  // Atualiza os pontos caso seja <jogador> vs "Bye".
                }
            }
        }

        return pairings;
    }

    private Player findMatchingPlayer(Player player, List<Player> players, Set<String> pairedPlayerIds, int startIndex) {
        if (startIndex >= players.size()) {
            return null;
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
        return eventPointsDiff + rankPointsDiff + matchWinrateDiff;  // O score é a soma das diferenças.
    }

    private static Comparator<Player> getRankComparator() {
        return Comparator.comparing(Player::getEventPoints, Comparator.reverseOrder())
                         .thenComparing(Player::getRankPoints);
    }

    private void updatePlayerForBye(String playerId) {
        playerRepository.findById(playerId).ifPresent(player -> {
            player.addEventPoints(1);  // Adiciona 1 ponto para a vitória.
            playerRepository.save(player);
        });
    }
}
