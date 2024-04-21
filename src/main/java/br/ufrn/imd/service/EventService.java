package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    // atualiza um evento com o ID fornecido, com base nos detalhes do evento passado no parâmetro
    public Event updateEvent(String id, Event eventDetails) {
        return getEventById(id).map(event -> {
            event.setName(eventDetails.getName());
            event.setDate(eventDetails.getDate());
            event.setLocation(eventDetails.getLocation());
            event.setNumberOfRounds(eventDetails.getNumberOfRounds());
            event.setPlayerIds(eventDetails.getPlayerIds());
            return eventRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found!"));
    }

    // adiciona um player ao evento, com base nos id's passados
    public Event addPlayerToEvent(String eventId, String playerId) {
        Optional<Event> eventOptional = getEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.addPlayerId(playerId);  // Adiciona o ID do jogador ao evento
            return eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
    }
}
