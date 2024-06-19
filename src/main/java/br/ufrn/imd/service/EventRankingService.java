package br.ufrn.imd.service;

import br.ufrn.imd.model.PlayerResult;
import br.ufrn.imd.strategy.EventRankingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRankingService {

    private final EventRankingStrategy eventRankingStrategy;

    @Autowired
    public EventRankingService(EventRankingStrategy eventRankingStrategy) {
        this.eventRankingStrategy = eventRankingStrategy;
    }

    public List<PlayerResult> sortByResultEventPoints(List<PlayerResult> playerResults) {
        return eventRankingStrategy.rankPlayerResults(playerResults);
    }
}
