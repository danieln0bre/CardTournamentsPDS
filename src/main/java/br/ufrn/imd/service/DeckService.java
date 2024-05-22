package br.ufrn.imd.service;

import br.ufrn.imd.model.Deck;
import br.ufrn.imd.repository.DeckRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to Decks.
 */
@Service
public class DeckService {

    private final DeckRepository deckRepository;

    /**
     * Constructs a DeckService with the necessary repository.
     *
     * @param deckRepository the repository used for deck operations
     */
    @Autowired
    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    /**
     * Checks if a deck exists in the repository by its ID.
     *
     * @param deckId the ID of the deck to check
     * @return true if an entity with the given id exists, false otherwise
     */
    public boolean existsById(String deckId) {
        return deckRepository.existsById(deckId);
    }

    /**
     * Retrieves a deck by its ID.
     *
     * @param id the ID of the deck to retrieve
     * @return an Optional containing the deck if found, or an empty Optional if not found
     */
    public Optional<Deck> findDeckById(String id) {
        return deckRepository.findById(id);
    }

    public List<Deck> getAllWinningDecks() {
        return deckRepository.findAll();
    }
    
    public Deck getDeckById(String deckId) {
        return deckRepository.findById(deckId).orElse(null);
    }
}
