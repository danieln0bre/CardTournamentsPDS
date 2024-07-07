package br.ufrn.imd.strategy;

import java.util.List;

public interface EventRankingStrategy<T, R> {
    List<T> rankEntities(List<T> entities);
    List<R> rankEntityResults(List<R> entityResults);
}
