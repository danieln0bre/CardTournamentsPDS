package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.util.PlayerValidationUtil;
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
        PlayerValidationUtil.validatePlayer(player);
        player = winrateService.calculateWinRates(player);
        return playerRepository.save(player);
    }

    public Player updatePlayer(String id, Player playerDetails) {
        PlayerValidationUtil.validatePlayer(playerDetails);
        playerDetails.setId(id);
        return createPlayer(playerDetails);
    }

    public void updatePlayerOpponents(String playerId, String opponentId) {
        if (opponentId == null || opponentId.isEmpty()) {
            throw new IllegalArgumentException("Opponent ID cannot be null or empty.");
        }
        
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        player.addOpponentId(opponentId);
        playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }

    public List<Player> getPlayersByIds(List<String> playerIds) {
        if (playerIds == null || playerIds.isEmpty()) {
            throw new IllegalArgumentException("Player IDs list cannot be null or empty.");
        }
        return playerRepository.findAllById(playerIds);
    }

    public Player addEventToPlayer(String playerId, String eventId) {
        if (eventId == null || eventId.isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty.");
        }
        
        Player player = getPlayerById(playerId).orElseThrow(() -> 
            new IllegalArgumentException("Player not found with ID: " + playerId));
        
        player.addEventId(eventId);
        return playerRepository.save(player);
    }
    
    // Verifica se todos os jogadores to evento tem decks registrados
    public boolean allPlayersHaveDecks(List<String> playerIds) {
        List<Player> players = getPlayersByIds(playerIds);
        return players.stream().allMatch(Player::hasDeck);
    }
    
    public void registerPlayerDeckName(String id, String deckName) {
    	
    }
}
