package br.ufrn.imd.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

//Especifica o collection "managers" que será usado pela classe Manager.
@Document(collection = "managers")
public class Manager extends User {
	
	// Usa a chave primária id da superclasse User.
	
	ArrayList<Event> eventos;
	
	public Manager(String name, String username, String email, String password) {
        super(name, username, email, password);
        this.eventos = new ArrayList<Event>();
    }

	// Gets e sets.
	
	public ArrayList<Event> getEventos() {
		return eventos;
	}

	public void setEventos(ArrayList<Event> eventos) {
		this.eventos = eventos;
	}
}
