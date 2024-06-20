package br.ufrn.imd.strategy;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import java.util.List;

public interface PairingStrategy {
    List<Pairing> createPairings(List<Player> players);
}
