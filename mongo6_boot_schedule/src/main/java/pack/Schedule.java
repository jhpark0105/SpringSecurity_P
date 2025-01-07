package pack;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="schedules")
public class Schedule {
	@Id
	private String id;
	
	private String title;
	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
