package br.ufrn.imd.service;

import br.ufrn.imd.model.Event;
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

    public Optional<Event> getEventById(String id) {  // Mudança de Long para String
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEvent(String id) {  // Mudança de Long para String
        eventRepository.deleteById(id);
    }

    // Este método updateEvent pode ser removido se você utilizar saveEvent para atualizações também
    public Event updateEvent(String id, Event eventDetails) {  // Mudança para inclusão do id como parâmetro
        return getEventById(id).map(event -> {
            event.setName(eventDetails.getName());
            event.setDate(eventDetails.getDate());
            event.setLocation(eventDetails.getLocation());
            event.setNumberOfRounds(eventDetails.getNumberOfRounds());
            event.setPlayers(eventDetails.getPlayers());
            return eventRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found!"));
    }
}
