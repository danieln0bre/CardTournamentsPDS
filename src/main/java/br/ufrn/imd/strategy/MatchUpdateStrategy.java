package br.ufrn.imd.strategy;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;

import java.util.List;
import java.util.Map;

public interface MatchUpdateStrategy {
    void updateMatchResult(Pairing pairing);
    Map<String, Map<String, Double>> getDeckMatchupStatistics(EventResult eventResult);
    void updateDeckMatchups(String eventId, List<Pairing> pairings);
}
