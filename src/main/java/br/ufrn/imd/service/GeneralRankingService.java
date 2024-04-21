package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class GeneralRankingService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getRankedPlayersByRankPoints() {
        List<Player> players = playerRepository.findAll();
        Collections.sort(players, new RankPointsComparator());
        return players;
    }

    static class RankPointsComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return Integer.compare(p2.getRankPoints(), p1.getRankPoints());
        }
    }
}
