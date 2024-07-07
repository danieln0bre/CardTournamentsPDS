package br.ufrn.imd.strategy.impl;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.GameObjectRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.strategy.MatchUpdateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultMatchUpdateStrategy implements MatchUpdateStrategy {

    private final GameObjectRepository gameObjectRepository;
    private final PlayerRepository playerRepository;
    private final Map<String, Map<String, Integer[]>> gameObjectMatchups = new HashMap<>();

    @Autowired
    public DefaultMatchUpdateStrategy(PlayerRepository playerRepository, GameObjectRepository gameObjectRepository) {
        this.playerRepository = playerRepository;
        this.gameObjectRepository = gameObjectRepository;
    }

    @Override
    public void updateMatchResult(Pairing pairing) {
        validatePairing(pairing);
        handleByeMatch(pairing);
        updatePlayersResults(pairing);
    }

    @Override
    public void validatePairing(Pairing pairing) {
        if (pairing == null) {
            throw new IllegalArgumentException("Pairing cannot be null.");
        }
        if (pairing.getResult() < 0 || pairing.getResult() > 1) {
            throw new IllegalArgumentException("Invalid match result. Must be 0 or 1.");
        }
    }

    @Override
    public void handleByeMatch(Pairing pairing) {
        if ("Bye".equals(pairing.getEntityTwoId())) {
            updatePlayerForBye(pairing.getEntityOneId());
        } else if ("Bye".equals(pairing.getEntityOneId())) {
            updatePlayerForBye(pairing.getEntityTwoId());
        }
    }

    @Override
    public void updatePlayersResults(Pairing pairing) {
        if ("Bye".equals(pairing.getEntityOneId()) || "Bye".equals(pairing.getEntityTwoId())) {
            return;
        }

        Player playerOne = fetchPlayer(pairing.getEntityOneId());
        Player playerTwo = fetchPlayer(pairing.getEntityTwoId());

        if (pairing.getResult() == 0) {
            playerOne.setEventPoints(playerOne.getEventPoints() + 1);
        } else if (pairing.getResult() == 1) {
            playerTwo.setEventPoints(playerTwo.getEventPoints() + 1);
        }

        playerRepository.save(playerOne);
        playerRepository.save(playerTwo);
    }

    @Override
    public void updateGameObjectMatchups(String eventId, List<Pairing> pairings) {
        Map<String, Map<String, Integer[]>> gameObjectMatchups = new HashMap<>();

        for (Pairing pairing : pairings) {
            String playerOneId = pairing.getEntityOneId();
            String playerTwoId = pairing.getEntityTwoId();

            Player playerOne = playerRepository.findById(playerOneId).orElse(null);
            Player playerTwo = playerRepository.findById(playerTwoId).orElse(null);

            if (playerOne == null || playerTwo == null) continue;

            String playerOneDeckId = playerOne.getGameObjectId();
            String playerTwoDeckId = playerTwo.getGameObjectId();

            gameObjectMatchups.putIfAbsent(playerOneDeckId, new HashMap<>());
            gameObjectMatchups.putIfAbsent(playerTwoDeckId, new HashMap<>());

            gameObjectMatchups.get(playerOneDeckId).putIfAbsent(playerTwoDeckId, new Integer[]{0, 0});
            gameObjectMatchups.get(playerTwoDeckId).putIfAbsent(playerOneDeckId, new Integer[]{0, 0});

            Integer[] resultsPlayerOne = gameObjectMatchups.get(playerOneDeckId).get(playerTwoDeckId);
            Integer[] resultsPlayerTwo = gameObjectMatchups.get(playerTwoDeckId).get(playerOneDeckId);

            if (pairing.getResult() == 0) {
                resultsPlayerOne[0]++;
            } else if (pairing.getResult() == 1) {
                resultsPlayerTwo[0]++;
            }

            resultsPlayerOne[1]++;
            resultsPlayerTwo[1]++;

            gameObjectMatchups.get(playerOneDeckId).put(playerTwoDeckId, resultsPlayerOne);
            gameObjectMatchups.get(playerTwoDeckId).put(playerOneDeckId, resultsPlayerTwo);
        }

        // Save or update the matchups in the repository or other storage if needed.
    }

    private Player fetchPlayer(String playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
    }

    private void updatePlayerForBye(String playerId) {
        Player player = fetchPlayer(playerId);
        player.setEventPoints(player.getEventPoints() + 1);
        playerRepository.save(player);
    }
}
