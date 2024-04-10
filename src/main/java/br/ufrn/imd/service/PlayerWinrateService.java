package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerWinrateService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerWinrateService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    private Player calculateOpponentsMatchWinrate(Player player) {
        List<String> opponentIds = player.getOpponentIds();
        if (opponentIds.isEmpty()) {
            player.setOpponentsMatchWinrate(0.0);
            return player;
        }

        List<Player> opponents = opponentIds.stream()
            .map(playerRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        double totalWinrate = opponents.stream()
            .mapToDouble(Player::getWinrate)
            .average()
            .orElse(0.0);

        player.setOpponentsMatchWinrate(totalWinrate);
        return playerRepository.save(player);
    }

    public Player updatePlayerWinrate(Player player) {
        int totalEvents = player.getOpponentIds().size();
        if (totalEvents > 0) {
            double winrate = (double) player.getEventPoints() / totalEvents;
            player.setWinrate(winrate);
        } else {
            player.setWinrate(0.0);
        }
        return playerRepository.save(player);
    }

    public Player calculateWinRates(Player player) {
        player = updatePlayerWinrate(player);
        player = calculateOpponentsMatchWinrate(player);
        return player;
    }
}
