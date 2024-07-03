package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.repository.TeamRepository;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    @Autowired
    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team addPlayerToTeam(String teamId, String playerId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Optional<Player> playerOptional = playerService.getPlayerById(playerId);

        if (teamOptional.isPresent() && playerOptional.isPresent()) {
            Team team = teamOptional.get();
            Player player = playerOptional.get();
            if (team.addPlayer(player)) {
                return teamRepository.save(team);
            }
        }
        return null;
    }

    public Team removePlayerFromTeam(String teamId, String playerId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Optional<Player> playerOptional = playerService.getPlayerById(playerId);

        if (teamOptional.isPresent() && playerOptional.isPresent()) {
            Team team = teamOptional.get();
            Player player = playerOptional.get();
            if (team.removePlayer(player)) {
                return teamRepository.save(team);
            }
        }
        return null;
    }
}
