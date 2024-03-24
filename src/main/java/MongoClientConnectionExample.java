import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
                MongoDatabase database = mongoClient.getDatabase("test");
                MongoCollection<Document> collection = database.getCollection("example");
                
                // Create a document to insert
                Document document = new Document("name", "John Doe")
                                        .append("age", 30)
                                        .append("email", "john.doe@example.com");
                
                // Insert the document into the collection
                collection.insertOne(document);
                
                System.out.println("Document inserted successfully!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}
