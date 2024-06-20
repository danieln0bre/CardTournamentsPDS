package br.ufrn.imd.service.impl;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.service.AbstractMatchService;
import br.ufrn.imd.strategy.StatisticsGenerationStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConcreteMatchService extends AbstractMatchService {

    private final PlayerRepository playerRepository;
    private final StatisticsGenerationStrategy statisticsGenerationStrategy;

    @Autowired
    public ConcreteMatchService(PlayerRepository playerRepository, StatisticsGenerationStrategy statisticsGenerationStrategy) {
        this.playerRepository = playerRepository;
        this.statisticsGenerationStrategy = statisticsGenerationStrategy;
    }

    @Override
    public void updateMatchResult(Pairing pairing) {
        validatePairing(pairing);
        handleByeMatch(pairing);
        updatePlayersResults(pairing);
    }

    @Override
    public Map<String, Map<String, Double>> getDeckMatchupStatistics(EventResult eventResult) {
        return statisticsGenerationStrategy.generateStatistics(eventResult);
    }

    @Override
    public void updateDeckMatchups(String eventId, List<Pairing> pairings) {
        Map<String, Map<String, Integer[]>> deckMatchups = new HashMap<>();

        for (Pairing pairing : pairings) {
            String playerOneId = pairing.getPlayerOneId();
            String playerTwoId = pairing.getPlayerTwoId();

            Player playerOne = playerRepository.findById(playerOneId).orElse(null);
            Player playerTwo = playerRepository.findById(playerTwoId).orElse(null);

            if (playerOne == null || playerTwo == null) continue;

            String playerOneDeckId = playerOne.getDeckId();
            String playerTwoDeckId = playerTwo.getDeckId();

            deckMatchups.putIfAbsent(playerOneDeckId, new HashMap<>());
            deckMatchups.putIfAbsent(playerTwoDeckId, new HashMap<>());

            deckMatchups.get(playerOneDeckId).putIfAbsent(playerTwoDeckId, new Integer[]{0, 0});
            deckMatchups.get(playerTwoDeckId).putIfAbsent(playerOneDeckId, new Integer[]{0, 0});

            Integer[] resultsPlayerOne = deckMatchups.get(playerOneDeckId).get(playerTwoDeckId);
            Integer[] resultsPlayerTwo = deckMatchups.get(playerTwoDeckId).get(playerOneDeckId);

            if (pairing.getResult() == 0) {
                resultsPlayerOne[0]++;
            } else if (pairing.getResult() == 1) {
                resultsPlayerTwo[0]++;
            }

            resultsPlayerOne[1]++;
            resultsPlayerTwo[1]++;

            deckMatchups.get(playerOneDeckId).put(playerTwoDeckId, resultsPlayerOne);
            deckMatchups.get(playerTwoDeckId).put(playerOneDeckId, resultsPlayerTwo);
        }

        // Save or update the matchups in the repository or other storage if needed.
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
        if ("Bye".equals(pairing.getPlayerTwoId())) {
            updatePlayerForBye(pairing.getPlayerOneId());
        } else if ("Bye".equals(pairing.getPlayerOneId())) {
            updatePlayerForBye(pairing.getPlayerTwoId());
        }
    }

    @Override
	public void updatePlayersResults(Pairing pairing) {
        if ("Bye".equals(pairing.getPlayerOneId()) || "Bye".equals(pairing.getPlayerTwoId())) {
            return;
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
        player.setEventPoints(player.getEventPoints() + 1);
        playerRepository.save(player);
    }
}
