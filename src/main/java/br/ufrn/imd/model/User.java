package br.ufrn.imd.model;

import org.springframework.data.annotation.Id;

public class User {
	
	// Chave primária do usuário utilizada no MongoDB.
	// Será utilizada nas subclasses Player e Manager.
	@Id
	private String id;
	
	private String name;
	private String username;
	private String password;
	private String email;
	


	public User(String name, String username, String email, String password) {
    	this.name = name;
    	this.username = username;
    	this.password = password;
    	this.email = email;
    }
	
	// Gets e sets.
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    public String getName() {
    	return name;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
