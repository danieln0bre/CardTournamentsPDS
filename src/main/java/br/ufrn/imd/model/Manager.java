package br.ufrn.imd.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "managers")
public class Manager extends User {

    private List<Event> events;

    public Manager(String name, String username, String email, String password) {
        super(name, username, email, password, Role.ROLE_MANAGER);
        this.events = new ArrayList<>();
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = new ArrayList<>(events);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }
}
