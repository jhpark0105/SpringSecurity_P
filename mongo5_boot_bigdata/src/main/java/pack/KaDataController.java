package pack;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class KaDataController {
    @Autowired
    private MongoClient mongoClient;
    
    @GetMapping("/")
    public String sijak() {
        return "index"; 
    }

    // /show 경로에 요청이 오면 show.html을 반환
    @GetMapping("/show")
    public String showChatData(Model model) {
        List<KaData> kaDataList = new ArrayList<>(); // 데이터를 저장할 리스트

        // MongoDB의 GridFSBucket에서 파일을 가져온다.
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase("katalkdb"), "katalkFiles");

        try {
            // GridFS에서 저장된 파일을 하나씩 가져온다.
            for (GridFSFile gridFSFile : gridFSBucket.find()) {
                ObjectId fileId = gridFSFile.getObjectId();

                // 파일 다운로드
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                gridFSBucket.downloadToStream(fileId, outputStream);
                String fileContent = new String(outputStream.toByteArray());

                // JSON 파싱
                if (fileContent.trim().startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(fileContent);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        KaData kaData = new KaData();
                        kaData.setReq(jsonObject.getString("req"));
                        kaData.setRes(jsonObject.getString("res"));
                        kaDataList.add(kaData);
                    }
                } else {
                    JSONObject jsonObject = new JSONObject(fileContent);
                    KaData kaData = new KaData();
                    kaData.setReq(jsonObject.getString("req"));
                    kaData.setRes(jsonObject.getString("res"));
                    kaDataList.add(kaData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("dataList", kaDataList);     // 데이터를 모델에 추가
        return "show";     // show.html 파일을 반환
    }
}
