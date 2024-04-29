package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final PlayerRepository playerRepository;

    @Autowired
    public MatchService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Updates the match result based on the provided pairing.
     * @param pairing The pairing information including player IDs and match result.
     */
    public void updateMatchResult(Pairing pairing) {
        validatePairing(pairing);
        handleByeMatch(pairing);
        updatePlayersResults(pairing);
    }

    private void validatePairing(Pairing pairing) {
        if (pairing == null) {
            throw new IllegalArgumentException("Pairing cannot be null.");
        }
        if (pairing.getResult() < 0 || pairing.getResult() > 1) {
            throw new IllegalArgumentException("Invalid match result. Must be 0 or 1.");
        }
    }

    private void handleByeMatch(Pairing pairing) {
        if ("Bye".equals(pairing.getPlayerTwoId())) {
            updatePlayerForBye(pairing.getPlayerOneId());
            return;
        } else if ("Bye".equals(pairing.getPlayerOneId())) {
            updatePlayerForBye(pairing.getPlayerTwoId());
            return;
        }
    }

    private void updatePlayersResults(Pairing pairing) {
        if ("Bye".equals(pairing.getPlayerOneId()) || "Bye".equals(pairing.getPlayerTwoId())) {
            return;  // Stop further processing if it's a bye.
        }

        Player playerOne = fetchPlayer(pairing.getPlayerOneId());
        Player playerTwo = fetchPlayer(pairing.getPlayerTwoId());

        if (pairing.getResult() == 0) {
            playerOne.setEventPoints(playerOne.getEventPoints() + 1);
        } else if (pairing.getResult() == 1) {
            playerTwo.setEventPoints(playerTwo.getEventPoints() + 1);
        }

        playerRepository.save(playerOne);
        playerRepository.save(playerTwo);
    }

    private Player fetchPlayer(String playerId) {
        return playerRepository.findById(playerId)
                               .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
    }

    private void updatePlayerForBye(String playerId) {
        Player player = fetchPlayer(playerId);
        player.setEventPoints(player.getEventPoints() + 1); // Automatically win for a bye.
        playerRepository.save(player);
    }
}
