package br.ufrn.imd.service;

import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.strategy.PairingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericPairingService {

    private final PairingStrategy<Player> playerPairingStrategy;
    private final PairingStrategy<Team> teamPairingStrategy;
    private final PlayerService playerService;
    private final TeamService teamService;

    @Autowired
    public GenericPairingService(PairingStrategy<Player> playerPairingStrategy,
                                 PairingStrategy<Team> teamPairingStrategy,
                                 PlayerService playerService,
                                 TeamService teamService) {
        this.playerPairingStrategy = playerPairingStrategy;
        this.teamPairingStrategy = teamPairingStrategy;
        this.playerService = playerService;
        this.teamService = teamService;
    }

    public List<Pairing> createPairings(String eventId, boolean isTeamEvent) {
        if (isTeamEvent) {
            List<Team> teams = teamService.getTeamsByEventId(eventId);
            return teamPairingStrategy.createPairings(teams);
        } else {
            List<Player> players = playerService.getPlayersByEventId(eventId);
            return playerPairingStrategy.createPairings(players);
        }
    }
}
