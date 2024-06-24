package br.ufrn.imd.strategy;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;

public interface RoundAndEventFinalizationStrategy {
    Event finalizeEvent(String eventId);
    Event finalizeRound(String eventId);
    EventResult getEventResultByEventId(String eventId);
}
