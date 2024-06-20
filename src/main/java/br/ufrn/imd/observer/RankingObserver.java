package br.ufrn.imd.observer;

import br.ufrn.imd.model.Event;

public interface RankingObserver {
    void updateRanking(Event event);
}
