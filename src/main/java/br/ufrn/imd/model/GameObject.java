package br.ufrn.imd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Map;
import java.util.HashMap;

@Document(collection = "winning_decks2")
public class GameObject {
    @Id
    private String id;

    @Field("deck_name")
    private String deckName;

    @Field("position_frequencies")
    private Map<Integer, Integer> positionFrequencies;

    public GameObject() {
        this.deckName = "";
        this.positionFrequencies = new HashMap<>();
    }

    public GameObject(String deckName, Map<Integer, Integer> positionFrequencies) {
        this.deckName = deckName;
        this.positionFrequencies = new HashMap<>(positionFrequencies);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public Map<Integer, Integer> getPositionFrequencies() {
        return new HashMap<>(positionFrequencies);
    }

    public void setPositionFrequencies(Map<Integer, Integer> positionFrequencies) {
        this.positionFrequencies = new HashMap<>(positionFrequencies);
    }
}
