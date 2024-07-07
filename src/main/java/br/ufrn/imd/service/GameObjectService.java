package br.ufrn.imd.service;

import br.ufrn.imd.model.GameObject;
import br.ufrn.imd.repository.GameObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return deckRepository.findAll();
    }

    public GameObject saveGameObject(GameObject deck) {
        return deckRepository.save(deck);
    }
}
