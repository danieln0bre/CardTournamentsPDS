package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.repository.EventRepository;
import br.ufrn.imd.repository.PlayerRepository;
import br.ufrn.imd.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;
    private final EventRepository eventRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService, EventRepository eventRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
        this.eventRepository = eventRepository;
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
    
    public boolean allTeamsHaveDecks(List<String> teamIds) {
        List<Team> teams = teamRepository.findAllById(teamIds);
        for (Team team : teams) {
            for (String playerId : team.getPlayerIds()) {
                Optional<Player> playerOptional = playerService.getPlayerById(playerId);
                if (playerOptional.isPresent() && !playerOptional.get().hasGameObject()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<Team> getTeamsByEventId(String eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return teamRepository.findAllById(event.getEntityIds());
        } else {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
    }

    public boolean existsById(String teamId) {
        return teamRepository.existsById(teamId);
    }

    public void addEventToTeam(String teamId, String eventId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            team.addEventId(eventId);
            teamRepository.save(team);
        } else {
            throw new IllegalArgumentException("Team not found with ID: " + teamId);
        }
    }

    public Optional<Team> getTeamById(String teamId) {
        return teamRepository.findById(teamId);
    }

    public List<Team> getTeamsByIds(List<String> teamIds) {
        return teamRepository.findAllById(teamIds);
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
    
    public List<Team> getEventTeams(String eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return teamRepository.findAllById(event.getEntityIds());
        } else {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
    }

    @Transactional
    public Team removePlayerFromTeam(String teamId, String playerId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Optional<Player> playerOptional = playerRepository.findById(playerId);

        if (teamOptional.isPresent() && playerOptional.isPresent()) {
            Team team = teamOptional.get();
            Player player = playerOptional.get();

            boolean removed = team.removePlayer(player);

            if (removed) {
                player.setTeamId(null);
                playerRepository.save(player);
                team = teamRepository.save(team);
                return team;
            }
        }
        return null;
    }
}
