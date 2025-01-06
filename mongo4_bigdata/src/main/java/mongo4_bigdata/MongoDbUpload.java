package mongo4_bigdata;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.opencsv.CSVReader;

public class MongoDbUpload {

	public static void main(String[] args) {
		String connString = "mongodb://localhost:27017";
		
		try (MongoClient mongoClient = MongoClients.create(connString)) {
			MongoDatabase database = mongoClient.getDatabase("katalkdb");
			
			//GridFSBucket 생성 (분산 저장용)
			GridFSBucket gridFSBucket = GridFSBuckets.create(database, "katalkFiles");
			
			// resource 폴더에서 CSV 파일 읽기
			ClassLoader classLoader = MongoDbUpload.class.getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("katalkdata.csv");
			
			if(inputStream != null) {
				uploadCSVtoMongoDB(inputStream, gridFSBucket);
			}else {
				System.out.println("CSV 찾기 실패");
			}
			
		} catch (Exception e) {
			System.out.println("err : " + e);
		}
	}
	
	private static void uploadCSVtoMongoDB(InputStream inputStream, GridFSBucket gridFSBucket) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				CSVReader csvReader = new CSVReader(reader)) {
			List<String[]> records = csvReader.readAll();
			
			for(String[] record : records) {
				Document doc = new Document("req", record[0]).append("res", record[1]);
				
				//대용량 자료를 1MB 크기의 청크(묶음)로 나누어 저장
				GridFSUploadOptions options = new GridFSUploadOptions().chunkSizeBytes(1024 * 1024);
				
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(doc.toJson().getBytes());
				
				ObjectId fileId = gridFSBucket.uploadFromStream("katalkdata", byteArrayInputStream, options);
				
				System.out.println("saved id : " + fileId.toHexString());
			}
		} catch (Exception e) {
			
		}
	}

}
