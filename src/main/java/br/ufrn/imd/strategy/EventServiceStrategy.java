package br.ufrn.imd.strategy;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.PlayerResult;

import java.util.List;
import java.util.Map;

public interface EventServiceStrategy {
    Event finalizeEvent(String eventId);
    EventResult getEventResultByEventId(String eventId);
    List<PlayerResult> getEventResultRanking(String eventId);
    Map<String, Map<String, Double>> getDeckMatchupStatistics(String eventId);
}
