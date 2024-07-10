package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("teamPairingService")
public class TeamPairingService implements PairingService {

    private final PairingStrategy<Team> pairingStrategy;
    private final TeamService teamService;

    @Autowired
    public TeamPairingService(PairingStrategy<Team> pairingStrategy, TeamService teamService) {
        this.pairingStrategy = pairingStrategy;
        this.teamService = teamService;
    }

    @Override
    public List<Pairing> createPairings(String eventId, boolean isTeamEvent) {
        List<Team> teams = teamService.getEventTeams(eventId);
        return pairingStrategy.createPairings(teams);
    }
}
