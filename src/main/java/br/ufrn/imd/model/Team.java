package br.ufrn.imd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "times")
public class Team {

    @Id
    private String id;
    private String name;
    private String ownerId;
    private List<Player> players;

    public Team(String name, String ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.players = new ArrayList<>(5); // Inicializa a lista com capacidade máxima de 5 jogadores
    }

    public boolean addPlayer(Player player) {
        if (players.size() < 5) {
            player.setTeam(this); // Set the team for the player
            return players.add(player);
        }
        return false; // Retorna false se já houver 5 jogadores no time
    }

    public boolean removePlayer(Player player) {
        player.setTeam(null); // Remove the team from the player
        return players.remove(player);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Retorna uma cópia da lista de jogadores
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

    public int getEventPoints() {
        return players.stream().mapToInt(Player::getEventPoints).sum();
    }

    public double getWinrate() {
        return players.stream().mapToDouble(Player::getWinrate).average().orElse(0.0);
    }
}
