package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Manager;

public class ManagerDAO {

	private ArrayList<Manager> managers;

	public ManagerDAO() {
		this.managers = new ArrayList<>();
	}
	
	public void adicionarManager(Manager manager) {
		managers.add(manager);
	}
	
	public void removerManager(Manager manager) {
		managers.remove(manager);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
