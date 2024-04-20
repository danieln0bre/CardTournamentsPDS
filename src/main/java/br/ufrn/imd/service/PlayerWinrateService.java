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

    // Calcular a winrate dos oponentes é importante para o sistema de ranking dos jogadores.
    private Player calculateOpponentsMatchWinrate(Player player) {
        List<String> opponentIds = player.getOpponentIds();
        
        // Se a lista de oponentes estiver vazia.
        
        if (opponentIds.isEmpty()) {
            player.setOpponentsMatchWinrate(0.0);
            return player;
        }

        // Se a lista de oponentes não está vazia.
        
        List<Player> opponents = opponentIds.stream().map(playerRepository::findById)
        											 .filter(Optional::isPresent)
        											 .map(Optional::get)
        											 .collect(Collectors.toList());
        
        double totalWinrate = opponents.stream().mapToDouble(Player::getWinrate)
        										.average()
        										.orElse(0.0);

        player.setOpponentsMatchWinrate(totalWinrate);
        
        return playerRepository.save(player);
    }

    
    public Player updatePlayerWinrate(Player player) {
        int totalOpponents = player.getOpponentIds().size();
        
        // Se não tiver oponentes.
        if(totalOpponents <= 0) {
        	player.setWinrate(0.0);
        	return playerRepository.save(player);
        }
        
        // Se tiver oponentes.
        double winrate = (double) player.getEventPoints() / totalOpponents;
        player.setWinrate(winrate);
        return playerRepository.save(player);
    }

    public Player calculateWinRates(Player player) {
        player = updatePlayerWinrate(player);
        player = calculateOpponentsMatchWinrate(player);
        return player;
    }
}
