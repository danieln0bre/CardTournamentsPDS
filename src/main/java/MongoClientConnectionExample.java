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

import java.util.List;
import java.util.Map;

public class MongoClientConnectionExample {

    public static void main(String[] args) {
        String connectionString = "mongodb+srv://danielnobre09:trabalhopds2024@cluster0.1dnub0i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("CardTournament"); // Change to your database name
                MongoCollection<Document> collection = database.getCollection("winning_decks"); // Change to your collection name

                // Instantiate Scraper class to scrape data
                String[] urls = {
                        "https://onepiecetopdecks.com/deck-list/jp-format-eb-01-memorial-set-op-07-500-years-into-the-future-decks/",
                        "https://onepiecetopdecks.com/deck-list/jp-format-op-06-twin-champions-decks/",
                        "https://onepiecetopdecks.com/deck-list/jp-format-op05-awakening-of-the-new-era/"
                };
                Scraper scraper = new Scraper(urls);
                scraper.scrape();

                // Retrieve deck positions
                Map<String, List<String>> deckPositions = scraper.getDeckPositions();

                // Insert deck positions into the collection
                for (Map.Entry<String, List<String>> entry : deckPositions.entrySet()) {
                    Document document = new Document("deck_name", entry.getKey())
                            .append("positions", entry.getValue());
                    collection.insertOne(document);
                }

                System.out.println("Winning decks inserted successfully!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}