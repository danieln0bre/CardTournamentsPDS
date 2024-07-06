package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.repository.TeamRepository;
import br.ufrn.imd.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public Team createTeam(String name, String ownerId) {
        Optional<Player> playerOptional = playerService.getPlayerById(ownerId);
        if (playerOptional.isPresent()) {
            Player owner = playerOptional.get();
            Team team = new Team(name, ownerId);
            team.setName(name);
            team.setOwnerId(ownerId);
            team.addPlayer(owner);
            Team savedTeam = teamRepository.save(team);
            
            // Update the player's teamId
            owner.setTeamId(savedTeam.getId());
            playerService.savePlayer(owner);
            
            return savedTeam;
        } else {
            throw new IllegalArgumentException("Player not found with ID: " + ownerId);
        }
    }
    
    public List<Player> getPlayers(Team team) {
        return team.getPlayerIds().stream()
                .map(playerRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    
    public int getEventPoints(Team team) {
        return team.getPlayerIds().stream()
                .map(playerRepository::findById)
                .filter(Optional::isPresent)
                .mapToInt(player -> player.get().getEventPoints())
                .sum();
    }

    public double getWinrate(Team team) {
        return team.getPlayerIds().stream()
                .map(playerRepository::findById)
                .filter(Optional::isPresent)
                .mapToDouble(player -> player.get().getWinrate())
                .average()
                .orElse(0.0);
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
    
    @Transactional
    public Team removePlayerFromTeam(String teamId, String playerId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Optional<Player> playerOptional = playerRepository.findById(playerId);

        if (teamOptional.isPresent() && playerOptional.isPresent()) {
            Team team = teamOptional.get();
            Player player = playerOptional.get();

            System.out.println("Tentando remover jogador do time...");
            System.out.println("Lista de jogadores antes da remoção: " + team.getPlayerIds());

            boolean removed = team.removePlayer(player);

            if (removed) {
                System.out.println("Jogador removido da lista de jogadores do time.");

                // Atualizando o jogador para remover o time
                player.setTeamId(null);
                playerRepository.save(player);

                // Salvando as alterações no time
                team = teamRepository.save(team);

                System.out.println("Lista de jogadores após a remoção: " + team.getPlayerIds());
                System.out.println("Time atualizado e salvo no banco de dados.");
                return team;
            } else {
                System.out.println("Jogador não encontrado na lista de jogadores do time.");
            }
        } else {
            if (!teamOptional.isPresent()) {
                System.out.println("Time não encontrado.");
            }
            if (!playerOptional.isPresent()) {
                System.out.println("Jogador não encontrado.");
            }
        }
        return null;
    }
}
