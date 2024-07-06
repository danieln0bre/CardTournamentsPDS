package br.ufrn.imd.controller;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.service.PlayerService;
import br.ufrn.imd.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    @Autowired
    public TeamController(TeamService teamService, PlayerService playerService, PlayerRepository playerRepository) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestParam String name, @RequestParam String ownerId) {
        Player owner = playerService.getPlayerById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + ownerId));
        
        if (owner.getId() == null) {
            throw new IllegalStateException("Player ID cannot be null");
        }
        
        Team team = new Team(name, ownerId);
        return ResponseEntity.ok(teamService.createTeam(name, ownerId));
    }

    @PostMapping("/{teamId}/add-player")
    public ResponseEntity<Team> addPlayerToTeam(@PathVariable String teamId, @RequestParam String playerId) {
        return ResponseEntity.of(Optional.ofNullable(teamService.addPlayerToTeam(teamId, playerId)));
    }
    
    @DeleteMapping("/{teamId}/remove-player/{playerId}")
    public ResponseEntity<Team> removePlayerFromTeam(@PathVariable String teamId, @PathVariable String playerId) {
        System.out.println("Recebida solicitação para remover jogador com ID: " + playerId + " do time com ID: " + teamId);
        Team updatedTeam = teamService.removePlayerFromTeam(teamId, playerId);
        if (updatedTeam != null) {
            return ResponseEntity.ok(updatedTeam);
        } else {
            System.out.println("Time ou jogador não encontrado ou falha ao remover jogador.");
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable String teamId) {
        Optional<Team> team = teamService.getTeamById(teamId);
        return team.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{playerId}/team")
    public ResponseEntity<String> getPlayerTeam(@PathVariable String playerId) {
        Player player = playerService.getPlayerById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        String teamId = player.getTeamId();
        return teamId != null ? ResponseEntity.ok(teamId) : ResponseEntity.notFound().build();
    }
}
