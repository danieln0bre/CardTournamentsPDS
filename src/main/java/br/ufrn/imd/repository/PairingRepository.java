package br.ufrn.imd.repository;

import java.util.ArrayList;
import br.ufrn.imd.model.Pairing;

public class PairingRepository {

	private ArrayList<Pairing> pairings;

	public PairingRepository() {
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
