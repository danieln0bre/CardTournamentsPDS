package br.ufrn.imd.strategy;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;

import java.util.List;

public interface MatchUpdateStrategy {
    void updateMatchResult(Pairing pairing);
    void validatePairing(Pairing pairing);
    void handleByeMatch(Pairing pairing);
    void updatePlayersResults(Pairing pairing);
    void updateDeckMatchups(String eventId, List<Pairing> pairings);
}
