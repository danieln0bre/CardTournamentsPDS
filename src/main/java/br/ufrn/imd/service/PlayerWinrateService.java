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

    // Calcula a winrate dos oponentes.
    private Player calculateOpponentsMatchWinrate(Player player) {
        List<String> opponentIds = player.getOpponentIds();
        
        // Verifica se a lista de oponentes é vazia.
        if (opponentIds.isEmpty()) {
            player.setOpponentsMatchWinrate(0.0);
            return player;
        }

        // Obtém os oponentes a partir dos IDs e calcula a média das winrates.
        List<Player> opponents = opponentIds.stream()
                                            .map(playerRepository::findById)
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .collect(Collectors.toList());

        if (opponents.isEmpty()) {
            throw new IllegalStateException("Oponentes não encontrados para os IDs fornecidos.");
        }

        double totalWinrate = opponents.stream()
                                       .mapToDouble(Player::getWinrate)
                                       .average()
                                       .orElse(0.0);

        player.setOpponentsMatchWinrate(totalWinrate);
        return playerRepository.save(player);
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
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        player = updatePlayerWinrate(player);
        player = calculateOpponentsMatchWinrate(player);
        return player;
    }
}
