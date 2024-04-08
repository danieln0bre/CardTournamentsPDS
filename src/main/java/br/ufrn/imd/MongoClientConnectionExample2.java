package br.ufrn.imd;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.ufrn.imd.service.Scraper;

import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoClientConnectionExample2 {

    public static void main(String[] args) {
        String connectionString = "mongodb+srv://danielnobre09:trabalhopds2024@cluster0.1dnub0i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                MongoDatabase database = mongoClient.getDatabase("CardTournament"); 
                MongoCollection<Document> collection = database.getCollection("winning_decks2"); 

                String[] urls = {
                        "https://onepiecetopdecks.com/deck-list/jp-format-eb-01-memorial-set-op-07-500-years-into-the-future-decks/",
                        "https://onepiecetopdecks.com/deck-list/jp-format-op-06-twin-champions-decks/",
                        "https://onepiecetopdecks.com/deck-list/jp-format-op05-awakening-of-the-new-era/"
                };
                Scraper scraper = new Scraper(urls);
                scraper.scrape();

                Map<String, List<String>> deckPositions = scraper.getDeckPositions();
                Map<String, Map<String, Integer>> deckPositionFrequencies = new HashMap<>();

                // Calculando a frequência das posições
                for (Map.Entry<String, List<String>> entry : deckPositions.entrySet()) {
                    Map<String, Integer> positionFrequencies = new HashMap<>();
                    for (String position : entry.getValue()) {
                        positionFrequencies.put(position, positionFrequencies.getOrDefault(position, 0) + 1);
                    }
                    deckPositionFrequencies.put(entry.getKey(), positionFrequencies);
                }

                // Inserindo no banco com a frequência das posições
                for (Map.Entry<String, Map<String, Integer>> entry : deckPositionFrequencies.entrySet()) {
                    Document document = new Document("deck_name", entry.getKey())
                            .append("position_frequencies", entry.getValue());
                    collection.insertOne(document);
                }

                System.out.println("Winning decks with position frequencies inserted successfully!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}
