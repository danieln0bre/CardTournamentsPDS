package br.ufrn.imd.strategy;

import br.ufrn.imd.model.EventResult;
import java.util.Map;

public interface StatisticsGenerationStrategy {
    Map<String, Map<String, Double>> generateStatistics(EventResult eventResult);
}
