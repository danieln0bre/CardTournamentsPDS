package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("playerPairingService")
public class PlayerPairingService implements PairingService {

    private final PairingStrategy<Player> pairingStrategy;
    private final PlayerService playerService;

    @Autowired
    public PlayerPairingService(PairingStrategy<Player> pairingStrategy, PlayerService playerService) {
        this.pairingStrategy = pairingStrategy;
        this.playerService = playerService;
    }

    @Override
    public List<Pairing> createPairings(String eventId) {
        List<Player> players = playerService.getPlayersByEventId(eventId);
        return pairingStrategy.createPairings(players);
    }
}
