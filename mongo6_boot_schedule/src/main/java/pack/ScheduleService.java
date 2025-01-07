package pack;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
	private final ScheduleRepository repository;
	
	public ScheduleService(ScheduleRepository repository) {
		this.repository = repository;
	}
	
	public List<Schedule> getAllSchedules(){
		return repository.findAll();
	}
	
	public Optional<Schedule> getScheduleById(String id){
		return repository.findById(id);
	}
	
	public Schedule createSchedule(Schedule schedule) {
		return repository.save(schedule);
	}
	
	public Schedule updateSchedule(String id, Schedule schedule) {
		schedule.setId(id);
		return repository.save(schedule);
	}
	
	public void deleteSchedule(String id) {
		repository.deleteById(id);
	}
}
