package br.ufrn.imd;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.ufrn.imd.model.Player;

import java.io.IOException;
import java.util.List;

import org.bson.Document;

public class DatabaseInserter {

    public void insertPlayers(List<Player> players) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://danielnobre09:trabalhopds2024@cluster0.1dnub0i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
            MongoDatabase database = mongoClient.getDatabase("CardTournament");
            MongoCollection<Document> collection = database.getCollection("players");

            for (Player player : players) {
                Document doc = new Document("id", player.getId())
                        .append("name", player.getName())
                        .append("username", player.getUsername())
                        .append("password", player.getPassword())
                        .append("rankPoints", player.getRankPoints())
                        .append("email", player.getUsername() + "@card.com");
                        // Add other Player attributes as needed

                collection.insertOne(doc);
            }
        }
    }

    public static void main(String[] args) {
        PlayerScraper scraper = new PlayerScraper();

        try {
            List<Player> players = scraper.scrapePlayers();
            new DatabaseInserter().insertPlayers(players);
            System.out.println(players.size() + " players have been inserted into the database.");
        } catch (IOException e) {
            System.err.println("An error occurred while scraping players: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
