package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    @Autowired
    private PlayerRepository playerRepository;

    public void playerWin(String playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(
            () -> new IllegalArgumentException("Player not found with ID: " + playerId));
        player.setEventPoints(player.getEventPoints() + 1);  // Increment event points
        playerRepository.save(player);
    }

    public void recordResult(String playerOneId, String playerTwoId, int result) {
        if (result == 0) {  // Player 1 wins
            playerWin(playerOneId);
        } else if (result == 1) {  // Player 2 wins
            playerWin(playerTwoId);
        }
    }
}
