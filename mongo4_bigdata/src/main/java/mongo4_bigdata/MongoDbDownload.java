package mongo4_bigdata;

import java.io.ByteArrayOutputStream;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.json.JSONArray;
import org.json.JSONObject;

public class MongoDbDownload {
	public static void main(String[] args) {
		//GridFS 로 저장된 MongoDB 자료 읽기
		String connString = "mongodb://localhost:27017";
		
		try (MongoClient mongoClient = MongoClients.create(connString)) {
			MongoDatabase database = mongoClient.getDatabase("katalkdb");
			
			GridFSBucket gridFSBucket = GridFSBuckets.create(database, "katalkFiles");
			
			MongoCursor<GridFSFile> cursor = gridFSBucket.find().iterator();
			
			while(cursor.hasNext()) {
				GridFSFile gridFSFile = cursor.next();
				
				ObjectId fileId = gridFSFile.getObjectId();
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				
				try {
					gridFSBucket.downloadToStream(fileId, outputStream);
					
					String fileContent = new String(outputStream.toByteArray());
					
					//System.out.println("fileContent : " + fileId.toHexString() + " : " + fileContent);
					
					//배열 형태의 경우
					if(fileContent.trim().startsWith("[")) {
						JSONArray jsonArray = new JSONArray(fileContent);
						
						for(int i = 0; i< jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							
							String req = jsonObject.getString("req");
							String res = jsonObject.getString("res");
							System.out.println("req:" + req + " , res:" + res);
						}
					} else {
						//단일 JSON 객체인 경우
						JSONObject jsonObject = new JSONObject(fileContent);
						String req = jsonObject.getString("req");
						String res = jsonObject.getString("res");
						System.out.println("req:" + req + " , res:" + res);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("err : " + e);
		}
	}
}
