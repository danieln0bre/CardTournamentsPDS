package br.ufrn.imd.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

//Especifica o collection "managers" que será usado pela classe Manager.
@Document(collection = "managers")
public class Manager extends User {
	ArrayList<Event> eventos;
	
	public Manager(String name, String username, String password) {
        super(name, username, password);
        this.eventos = new ArrayList<Event>();
    }
}
