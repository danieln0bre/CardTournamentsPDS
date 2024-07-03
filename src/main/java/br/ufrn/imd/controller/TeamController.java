package br.ufrn.imd.controller;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
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

    @Autowired
    public TeamController(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestParam String name, @RequestParam String ownerId) {
        Player owner = playerService.getPlayerById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + ownerId));
        Team team = new Team(name, ownerId);
        return ResponseEntity.ok(teamService.createTeam(team));
    }

    @PostMapping("/{teamId}/add-player")
    public ResponseEntity<Team> addPlayerToTeam(@PathVariable String teamId, @RequestParam String playerId) {
        return ResponseEntity.of(Optional.ofNullable(teamService.addPlayerToTeam(teamId, playerId)));
    }

    @DeleteMapping("/{teamId}/remove-player")
    public ResponseEntity<Team> removePlayerFromTeam(@PathVariable String teamId, @RequestParam String playerId) {
        return ResponseEntity.of(Optional.ofNullable(teamService.removePlayerFromTeam(teamId, playerId)));
    }

    @GetMapping("/{playerId}/team")
    public ResponseEntity<Team> getPlayerTeam(@PathVariable String playerId) {
        Player player = playerService.getPlayerById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        Team team = player.getTeam();
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }
}
