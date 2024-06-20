package br.ufrn.imd.service;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;

import java.util.List;
import java.util.Map;

public interface MatchService {
    void updateMatchResult(Pairing pairing);
    Map<String, Map<String, Double>> getDeckMatchupStatistics(EventResult eventResult);
    void updateDeckMatchups(String eventId, List<Pairing> pairings);

    // Método template
    default void processMatchResults(List<Pairing> pairings) {
        for (Pairing pairing : pairings) {
            validatePairing(pairing);
            handleByeMatch(pairing);
            updatePlayersResults(pairing);
        }
    }

    // Métodos a serem implementados pelas subclasses
    void validatePairing(Pairing pairing);
    void handleByeMatch(Pairing pairing);
    void updatePlayersResults(Pairing pairing);
}
