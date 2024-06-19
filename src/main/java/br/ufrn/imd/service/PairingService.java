package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PairingService {

    private final PairingStrategy pairingStrategy;

    @Autowired
    public PairingService(PairingStrategy pairingStrategy) {
        this.pairingStrategy = pairingStrategy;
    }

    public List<Pairing> createPairings(List<Player> players) {
        return pairingStrategy.createPairings(players);
    }
}
