package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import java.util.List;

public interface PairingService {
    List<Pairing> createPairings(String eventId, boolean isTeamEvent);
}
