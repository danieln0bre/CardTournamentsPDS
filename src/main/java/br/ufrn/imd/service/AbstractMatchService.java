package br.ufrn.imd.service;

import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Pairing;

import java.util.List;
import java.util.Map;

public abstract class AbstractMatchService implements MatchService {

    @Override
    public void processMatchResults(List<Pairing> pairings) {
        for (Pairing pairing : pairings) {
            validatePairing(pairing);
            handleByeMatch(pairing);
            updatePlayersResults(pairing);
        }
    }

    @Override
    public abstract void updateMatchResult(Pairing pairing);

    @Override
    public abstract Map<String, Map<String, Double>> getDeckMatchupStatistics(EventResult eventResult);

    @Override
    public abstract void updateDeckMatchups(String eventId, List<Pairing> pairings);

    public abstract void validatePairing(Pairing pairing);

    public abstract void handleByeMatch(Pairing pairing);

    public abstract void updatePlayersResults(Pairing pairing);
}
