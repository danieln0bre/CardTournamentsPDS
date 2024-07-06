package br.ufrn.imd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "times")
public class Team {

    @Id
    private String id;
    private String name;
    private String ownerId;
    @JsonManagedReference
    private List<String> playerIds = new ArrayList<>(5);
    private int eventPoints;

    public Team(String name, String ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.playerIds = new ArrayList<>(5); // Inicializa a lista com capacidade máxima de 5 jogadores
    }

    public boolean addPlayer(Player player) {
        if (playerIds.size() < 5) {
            player.setTeamId(this.id);
            return playerIds.add(player.getId());
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        player.setTeamId(null);
        return playerIds.remove(player.getId());
    }

    public List<String> getPlayerIds() {
        return new ArrayList<>(playerIds);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public void setEventPoints(int eventPoints) {
		this.eventPoints = eventPoints;
	}

}
