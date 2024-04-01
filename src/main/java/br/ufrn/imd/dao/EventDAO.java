package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Event;

public class EventDAO {
	
	private ArrayList<Event> events;

	public EventDAO() {
		this.events = new ArrayList<>();
	}
	
	public void adicionarEvento(Event evento) {
		events.add(evento);
	}
	
	public void removerEvento(Event evento) {
		events.remove(evento);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
