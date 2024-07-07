package br.ufrn.imd.service;

import br.ufrn.imd.strategy.EventRankingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRankingService<T, R> {
    private final EventRankingStrategy<T, R> eventRankingStrategy;

    @Autowired
    public EventRankingService(EventRankingStrategy<T, R> eventRankingStrategy) {
        this.eventRankingStrategy = eventRankingStrategy;
    }

    public List<R> sortByResultEventPoints(List<R> entityResults) {
        return eventRankingStrategy.rankEntityResults(entityResults);
    }

    public List<T> sortByEventPoints(List<T> entities) {
        return eventRankingStrategy.rankEntities(entities);
    }
}
