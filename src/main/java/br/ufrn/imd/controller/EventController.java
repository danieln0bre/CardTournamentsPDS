package br.ufrn.imd.controller;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Criar novo evento.
    @PostMapping("/")
    public Event createEvent(@RequestBody Event event) {
        return eventService.saveEvent(event);
    }

    // Retornar todos os eventos.
    @GetMapping("/")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Retornar um único evento por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id)
                		   .map(event -> ResponseEntity.ok(event))
                		   .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    // Atualizar evento.
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event eventDetails) {
        return eventService.getEventById(id)
                		   .map(event -> {  event.setName(eventDetails.getName());
                		   				    event.setDate(eventDetails.getDate());
        		   				   		    event.setLocation(eventDetails.getLocation());
        		   				   		    event.setNumberOfRounds(eventDetails.getNumberOfRounds());
        		   				   		    event.setPlayerIds(eventDetails.getPlayerIds());  // Update to use setPlayerIds
        		   				   		    Event updatedEvent = eventService.saveEvent(event);
        		   				   		    return ResponseEntity.ok(updatedEvent);
        		   				   		 })
                		   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar um evento.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        return eventService.getEventById(id)
                		   .map(event -> {
                			   				eventService.deleteEvent(id);
                			   				return ResponseEntity.ok().<Void>build();
                		   				 })
                		   .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
