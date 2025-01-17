package mongo1_basic;

import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest3 {
	public static void main(String[] args) {
		String uri = "mongodb://localhost:27017";
		try (MongoClient mongoClient = MongoClients.create(uri)){
			MongoDatabase db = mongoClient.getDatabase("test");
			MongoCollection<Document> collection = db.getCollection("customer");
			
			//동적인 칼럼 처리
			for(Document doc : collection.find()) {
				for(Entry<String, Object> entry : doc.entrySet()) {
					String fieldName = entry.getKey();
					Object fieldValue = entry.getValue();
					System.out.println(fieldName + " " + fieldValue);
				}
				
				
			}
				System.out.println("-----------------------------------");
				System.out.println("건수 : " + collection.countDocuments());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
