package br.ufrn.imd.dao;

import java.util.ArrayList;
import br.ufrn.imd.model.Pairing;

public class PairingDAO {

	private ArrayList<Pairing> pairings;

	public PairingDAO() {
		this.pairings = new ArrayList<>();
	}
	
	public void adicionarPairing(Pairing pairing) {
		pairings.add(pairing);
	}
	
	public void removerPairing(Pairing pairing) {
		pairings.remove(pairing);
	}
	
	public void mandarDadosParaMongoDB() {
		
	}
	
	public void receberDadosDoMongoDB() {
		
	}
}
