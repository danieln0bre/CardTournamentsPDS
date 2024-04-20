package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerWinrateService winrateService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerWinrateService winrateService) {
        this.playerRepository = playerRepository;
        this.winrateService = winrateService;
    }

    public Player createPlayer(Player player) {
        // Calculate win rates before saving the player
        player = winrateService.calculateWinRates(player);
        return playerRepository.save(player);
    }
    
    public Player updatePlayer(String id, Player playerDetails) {
        playerDetails.setId(id);  // Ensure the correct player is updated
        return createPlayer(playerDetails);  // Reuse createPlayer for recalculating win rates
    }
    
    public void updatePlayerOpponents(String playerId, String opponentId) {
        Player player = playerRepository.findById(playerId).orElseThrow(
            () -> new IllegalArgumentException("Player not found with ID: " + playerId));
        player.addOpponentId(opponentId);  // Adds the opponent ID to the player
        playerRepository.save(player);  // Saves the updated player
    }

    public Optional<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }
    
    public List<Player> getPlayersByIds(List<String> playerIds) {
        return playerRepository.findAllById(playerIds);
    }
    
    public Player addEventToPlayer(String playerId, String eventId) {
        Optional<Player> playerOptional = getPlayerById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.addEventId(eventId);  // Adds the event ID to the player's list
            return playerRepository.save(player);
        } else {
            throw new IllegalArgumentException("Player not found with ID: " + playerId);
        }
    }
}
