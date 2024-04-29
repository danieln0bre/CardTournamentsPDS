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

    // Calculates and updates the winrate based on opponents' winrates.
    private Player calculateOpponentsMatchWinrate(Player player) {
        List<String> opponentIds = player.getOpponentIds();
        if (opponentIds.isEmpty()) {
            player.setOpponentsMatchWinrate(0.0);
            return playerRepository.save(player);
        }

        List<Player> opponents = fetchPlayers(opponentIds);
        double averageWinrate = calculateAverageWinrate(opponents);
        player.setOpponentsMatchWinrate(averageWinrate);
        return playerRepository.save(player);
    }

    private List<Player> fetchPlayers(List<String> ids) {
        List<Player> players = ids.stream()
                                  .map(playerRepository::findById)
                                  .filter(Optional::isPresent)
                                  .map(Optional::get)
                                  .collect(Collectors.toList());

        if (players.isEmpty()) {
            throw new IllegalStateException("No opponents found for the provided IDs.");
        }
        return players;
    }

    private double calculateAverageWinrate(List<Player> players) {
        return players.stream()
                      .mapToDouble(Player::getWinrate)
                      .average()
                      .orElse(0.0);
    }

    public Player updatePlayerWinrate(Player player) {
        List<String> opponentIds = player.getOpponentIds();
        if (opponentIds.isEmpty()) {
            player.setWinrate(0.0);
            return playerRepository.save(player);
        }

        double winrate = (double) player.getEventPoints() / opponentIds.size();
        player.setWinrate(winrate);
        return playerRepository.save(player);
    }

    public Player calculateWinRates(Player player) {
        validatePlayer(player);
        player = updatePlayerWinrate(player);
        return calculateOpponentsMatchWinrate(player);
    }

    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
    }
}
