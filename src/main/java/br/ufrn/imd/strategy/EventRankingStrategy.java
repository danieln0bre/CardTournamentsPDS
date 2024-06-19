package br.ufrn.imd.strategy;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.PlayerResult;

import java.util.List;

public interface EventRankingStrategy {
    List<Player> rankPlayers(List<Player> players);
    List<PlayerResult> rankPlayerResults(List<PlayerResult> playerResults);
}
