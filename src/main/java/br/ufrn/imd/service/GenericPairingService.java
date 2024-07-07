package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericPairingService {

    private final PairingStrategy<Team> teamPairingStrategy;
    private final TeamService teamService;

    @Autowired
    public GenericPairingService(PairingStrategy<Team> teamPairingStrategy, TeamService teamService) {
        this.teamPairingStrategy = teamPairingStrategy;
        this.teamService = teamService;
    }

    public List<Pairing> createPairings(String eventId, boolean isTeamEvent) {
        if (isTeamEvent) {
            List<Team> teams = teamService.getTeamsByEventId(eventId);
            return teamPairingStrategy.createPairings(teams);
        } else {
            throw new UnsupportedOperationException("Player pairings not supported in this implementation.");
        }
    }
}
