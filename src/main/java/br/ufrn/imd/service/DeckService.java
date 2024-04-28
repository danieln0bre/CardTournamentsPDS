package br.ufrn.imd.service;

import br.ufrn.imd.model.Deck;
import br.ufrn.imd.repository.DeckRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    public boolean existsById(String deckId) {
        return deckRepository.existsById(deckId);
    }
    
    public Optional<Deck> findDeckById(String id) {
    	return deckRepository.findById(id);
    }
}
