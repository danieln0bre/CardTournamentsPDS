package br.ufrn.imd.controller;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.EventResult;
import br.ufrn.imd.model.Team;
import br.ufrn.imd.model.TeamResult;
import br.ufrn.imd.service.EventRankingService;
import br.ufrn.imd.service.EventService;
import br.ufrn.imd.service.ManagerService;
import br.ufrn.imd.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final ManagerService managerService;
    private final TeamService teamService;
    private final EventRankingService<Team, TeamResult> teamEventRankingService;

    @Autowired
    public EventController(EventService eventService, ManagerService managerService, TeamService teamService,
                           EventRankingService<Team, TeamResult> teamEventRankingService) {
        this.eventService = eventService;
        this.managerService = managerService;
        this.teamService = teamService;
        this.teamEventRankingService = teamEventRankingService;
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(@RequestParam String managerId, @RequestBody Event event) {
        if (event.getNumberOfRounds() < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        event.setManagerId(managerId);
        Event savedEvent = eventService.saveEvent(event);
        managerService.updateManagerEvents(savedEvent);
        return ResponseEntity.ok(savedEvent);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event eventDetails) {
        Optional<Event> existingEventOpt = eventService.getEventById(id);
        if (existingEventOpt.isPresent()) {
            Event existingEvent = existingEventOpt.get();
            existingEvent.updateDetailsFrom(eventDetails);
            Event updatedEvent = eventService.saveEvent(existingEvent);
            managerService.updateManagerEvents(updatedEvent);
            return ResponseEntity.ok(updatedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Event> getEventByName(@PathVariable String name) {
        Optional<Event> eventOpt = eventService.getEventByName(name.replace("-", " "));
        if (eventOpt.isPresent()) {
            return ResponseEntity.ok(eventOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            return ResponseEntity.ok(eventOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/rankings")
    public ResponseEntity<List<Team>> getEventRankings(@PathVariable String id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            List<Team> teams = teamService.getTeamsByIds(event.getEntityIds());
            return ResponseEntity.ok(teamEventRankingService.sortByEventPoints(teams));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{eventId}/finalize")
    public ResponseEntity<?> finalizeEvent(@PathVariable String eventId) {
        try {
            Event event = eventService.finalizeEvent(eventId);
            return ResponseEntity.ok("Event finalized successfully with ID: " + event.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error: Unable to finalize event.");
        }
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<EventResult> getEventResults(@PathVariable String id) {
        try {
            EventResult eventResult = eventService.getEventResultByEventId(id);
            return ResponseEntity.ok(eventResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/{id}/result-ranking")
    public ResponseEntity<List<TeamResult>> getEventResultRanking(@PathVariable String id) {
        try {
            List<TeamResult> ranking = eventService.getEventResultRanking(id);
            return ResponseEntity.ok(ranking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/{id}/gameobject-matchups")
    public ResponseEntity<Map<String, Map<String, Double>>> getGameObjectMatchups(@PathVariable String id) {
        try {
            Map<String, Map<String, Double>> matchups = eventService.getDeckMatchupStatistics(id);
            return ResponseEntity.ok(matchups);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/manager/{managerId}/events")
    public ResponseEntity<List<Event>> getEventsByManagerId(@PathVariable String managerId) {
        try {
            List<Event> events = managerService.getManagerEvents(managerId);
            return ResponseEntity.ok(events);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
