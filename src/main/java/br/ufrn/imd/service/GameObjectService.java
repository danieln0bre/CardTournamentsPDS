package br.ufrn.imd.service;

import br.ufrn.imd.model.GameObject;
import br.ufrn.imd.repository.GameObjectRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameObjectService {

    private final GameObjectRepository deckRepository;

    @Autowired
    public GameObjectService(GameObjectRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public GameObject getGameObjectById(String deckId) {
        return deckRepository.findById(deckId).orElse(null);
    }

    public List<GameObject> getAllGameObjects() {
        return deckRepository.findAll(); // Update this if there's a specific query for winning decks
    }

    public GameObject saveGameObject(GameObject deck) {
        return deckRepository.save(deck);
    }
}
