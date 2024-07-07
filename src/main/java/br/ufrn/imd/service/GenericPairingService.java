package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericPairingService {

    private final PairingStrategy<Player> playerPairingStrategy;
    private final PlayerService playerService;

    @Autowired
    public GenericPairingService(PairingStrategy<Player> playerPairingStrategy, PlayerService playerService) {
        this.playerPairingStrategy = playerPairingStrategy;
        this.playerService = playerService;
    }

    public List<Pairing> createPairings(String eventId) {
        List<Player> players = playerService.getPlayersByEventId(eventId);
        return playerPairingStrategy.createPairings(players);
    }
}
