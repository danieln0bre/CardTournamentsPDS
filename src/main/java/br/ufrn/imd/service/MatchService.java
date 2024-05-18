package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.DeckRepository;
import br.ufrn.imd.repository.PlayerRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

	private final DeckRepository deckRepository;
    private final PlayerRepository playerRepository;
    private final Map<String, Map<String, Integer[]>> deckMatchups = new HashMap<>();
    private Map<String, Map<String, Map<String, Integer[]>>> eventDeckMatchups;

    @Autowired
    public MatchService(PlayerRepository playerRepository, DeckRepository deckRepository) {
        this.playerRepository = playerRepository;
        this.deckRepository = deckRepository;
        this.eventDeckMatchups = new HashMap<>();
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

    public Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId) {
        Map<String, Map<String, Integer[]>> deckMatchups = eventDeckMatchups.getOrDefault(eventId, new HashMap<>());

        Map<String, Map<String, Double>> winPercentageMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer[]>> entry : deckMatchups.entrySet()) {
            String deckId = entry.getKey();
            Map<String, Double> opponentWinPercentages = new HashMap<>();

            for (Map.Entry<String, Integer[]> opponentEntry : entry.getValue().entrySet()) {
                String opponentDeckId = opponentEntry.getKey();
                Integer[] results = opponentEntry.getValue();
                if (results[1] != 0) {  // Prevent division by zero
                    double winPercentage = (double) results[0] / results[1] * 100;
                    opponentWinPercentages.put(opponentDeckId, winPercentage);
                }
            }

            winPercentageMap.put(deckId, opponentWinPercentages);
        }

        return winPercentageMap;
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

        registerDeckMatchup(playerOne.getDeckId(), playerTwo.getDeckId(), pairing.getResult());

        if (pairing.getResult() == 0) {
            playerOne.setEventPoints(playerOne.getEventPoints() + 1);
        } else if (pairing.getResult() == 1) {
            playerTwo.setEventPoints(playerTwo.getEventPoints() + 1);
        }

        playerRepository.save(playerOne);
        playerRepository.save(playerTwo);
    }

    private void registerDeckMatchup(String deckOneId, String deckTwoId, int result) {
        deckMatchups.putIfAbsent(deckOneId, new HashMap<>());
        deckMatchups.putIfAbsent(deckTwoId, new HashMap<>());

        deckMatchups.get(deckOneId).putIfAbsent(deckTwoId, new Integer[] {0, 0});
        deckMatchups.get(deckTwoId).putIfAbsent(deckOneId, new Integer[] {0, 0});

        if (result == 0) {
            deckMatchups.get(deckOneId).get(deckTwoId)[0]++;  // Increment wins for deckOne
        } else {
            deckMatchups.get(deckTwoId).get(deckOneId)[0]++;  // Increment wins for deckTwo
        }
        deckMatchups.get(deckOneId).get(deckTwoId)[1]++;  // Increment games played between decks
        deckMatchups.get(deckTwoId).get(deckOneId)[1]++;
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
