package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.User;

public class UserDAO {
	private ArrayList<User> users;

	public UserDAO() {
		this.users = new ArrayList<>();
	}
	
	public void adicionarManager(User user) {
		users.add(user);
	}
	
	public void removerManager(User user) {
		users.remove(user);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
