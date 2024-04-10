package br.ufrn.imd.model;

import java.util.ArrayList;

public class Manager extends User {
	ArrayList<Event> eventos;
	public Manager(String name, String username, String password) {
        super(name, username, password);
        this.eventos = new ArrayList<Event>();
    }

}
