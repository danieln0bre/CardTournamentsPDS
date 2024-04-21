package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    @Autowired
    private PlayerRepository playerRepository;

    // Atualiza o resultado da partida
    public void updateMatchResult(Pairing pairing) {
        // Verifica se o pareamento inclui "Bye"
        if (pairing.getPlayerTwoId().equals("Bye")) {
            updatePlayerForBye(pairing.getPlayerOneId());
            return;  // Encerra a atualização se um dos jogadores é "Bye"
        } else if (pairing.getPlayerOneId().equals("Bye")) {
            updatePlayerForBye(pairing.getPlayerTwoId());
            return;  // Encerra a atualização se um dos jogadores é "Bye"
        }

        Player playerOne = playerRepository.findById(pairing.getPlayerOneId())
                                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + pairing.getPlayerOneId()));
        Player playerTwo = playerRepository.findById(pairing.getPlayerTwoId())
                                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + pairing.getPlayerTwoId()));

        if (pairing.getResult() == 0) {  // Player 1 wins
            playerOne.setEventPoints(playerOne.getEventPoints() + 1);
        } else if (pairing.getResult() == 1) {  // Player 2 wins
            playerTwo.setEventPoints(playerTwo.getEventPoints() + 1);
        }

        playerRepository.save(playerOne);
        playerRepository.save(playerTwo);
    }

    private void updatePlayerForBye(String playerId) {
        Player player = playerRepository.findById(playerId)
                        .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        player.setEventPoints(player.getEventPoints() + 1);  // Assume 3 points for a win against "Bye"
        playerRepository.save(player);
    }
}
