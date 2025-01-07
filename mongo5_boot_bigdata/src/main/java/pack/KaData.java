package pack;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "katalkFiles")
public class KaData {
	@Id
	private String id;
	
	private String req;
	private String res;
}
