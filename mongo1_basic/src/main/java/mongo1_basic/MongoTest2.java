package mongo1_basic;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Filters.eq;


public class MongoTest2 {
	
	static class Customer {
		private ObjectId id;
		private String name;
		private int age;
		private String gender;
		
		public Customer(ObjectId id, String name, int age, String gender) {
			this.id = id;
			this.name = name;
			this.age = age;
			this.gender = gender;
		}
		
		@Override
		public String toString() {
			return "id: " + id + " , name : " + name + " , age : " + age + " , gender : " + gender;
		}
	}
	
	
	
	public static void main(String[] args) {
		//MongoDB 연동 후 CRUD 연습
		String uri = "mongodb://localhost:27017";
		try(MongoClient mongoClient = MongoClients.create(uri)){
			MongoDatabase db = mongoClient.getDatabase("test");
			MongoCollection<Document> collection = db.getCollection("customer");
			
			//추가
			//Document newData = new Document("name","신기해").append("age",31).append("gender", "남성");
			//collection.insertOne(newData);
			
			//수정, 삭제 대상 Document 검색
			Document fCustomer = collection.find(eq("name","신기해")).first();
			
			if(fCustomer != null) {
				ObjectId cusId = fCustomer.getObjectId("_id");
				
				//수정 : 나이와 성별 수정
				collection.updateOne(eq("_id", cusId),
					Updates.combine(Updates.set("age", 19), Updates.set("gender", "여성"))
				);
				
				
				//삭제
				collection.deleteOne(eq("_id",cusId));
			} else {
				System.out.println("해당 고객은 없어요");
			}
			
			//읽기
			List<Customer> clist = new ArrayList<MongoTest2.Customer>();
			
			for(Document doc : collection.find()) {
				//age 필드가 Integer 또는 String 일 수 있으므로 약간의 처리 필요
				Object ageObj = doc.get("age");
				int age = 0;
				if(ageObj instanceof Integer) {
					age = (Integer)ageObj;
				} else if (ageObj instanceof String) {
					try {
						age = Integer.parseInt((String)ageObj);
					} catch (NumberFormatException e) {
						System.out.println("invalid format : " + ageObj);
					}
				}
				
				Customer customer = new Customer(doc.getObjectId("_id"), doc.getString("name"), age, doc.getString("gender"));
				clist.add(customer);
			}
			
			for(Customer cus : clist) {
				System.out.println(cus.id + " " + cus.name + " " + cus.age + " " + cus.gender);
			}
			
			System.out.println("건수 : " + collection.countDocuments());
		}
	}
}
