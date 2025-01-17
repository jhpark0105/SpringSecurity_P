package mongo1_basic;

import java.util.function.Consumer;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoTest1 {
	public static void main(String[] args) {
		MongoClient client = MongoClients.create("mongodb://localhost:27017"); //mongodb 연결 객체 생성
		
		try {
			MongoDatabase db = client.getDatabase("test");
			MongoCollection<Document> collection = db.getCollection("customer");
			System.out.println("자료 건수 : " + collection.countDocuments());
			
			//첫번째 자료 읽기
			Document document = collection.find().first();
			System.out.println("첫번째 자료 : " + document);
			
			System.out.println("전체 자료 ---------------");
			//FindIterable<Document> iter = collection.find();
			//MongoCursor<Document> cursor = iter.iterator();
			MongoCursor<Document> cursor = collection.find().iterator();
			//MongoCursor<Document> cursor = collection.find().limit(2).iterator();
			
			while(cursor.hasNext()) {
				//Document document2 = cursor.next();
				//String jsonResult = document2.toJson();
				String jsonResult = cursor.next().toString();
				System.out.println(jsonResult);
			}
			
			System.out.println();
			
			//cursor가 소진되었기 때문에 다시 읽어줘야한다
			cursor = collection.find().iterator();
			while(cursor.hasNext()) {
				Document doc = cursor.next();
				System.out.println("이름 : " + doc.get("name") 
				+ " , 나이 : " + doc.get("age") + " , 성별 : " + doc.get("gender"));
			}
			
			System.out.println("Consumer -------------------------------------------");
			collection.find().forEach(printData);
			
			cursor.close();
			client.close();
		} catch (Exception e) {
			client.close();
			System.out.println("err : " + e.getMessage());
		}
	}
	
	//Block<Document> 대신에 Consumer<Document> 사용
	static Consumer<Document> printData = new Consumer<Document>() {
		@Override
		public void accept(final Document document) {
			//System.out.println(document.toJson());
			String name = document.getString("name");
			Integer age = document.getInteger("age");
			System.out.println(name + " 님의 나이는 " + age);
		}
	};
}
