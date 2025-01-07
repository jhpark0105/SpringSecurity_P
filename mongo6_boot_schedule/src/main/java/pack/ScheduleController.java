package pack;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {
	private final ScheduleService service;
	
	public ScheduleController(ScheduleService service) {
		this.service = service;
	}
	
	@GetMapping
	public String getAllSchedules(Model model) {
		List<Schedule> schedules = service.getAllSchedules();
		
		model.addAttribute("schedules",schedules);
		return "schedule/list";
	}
	
	@GetMapping("/new")
	public String createform(Model model) {
		model.addAttribute("schedule", new Schedule());
		return "schedule/create"; //create.html에서 Schedule 객체 접근 가능
	}
	
	@PostMapping
	public String createschedule(@ModelAttribute("schedule") Schedule schedule) { //ModelAttribute는 다른 어노테이션보다 먼저 수행
		service.createSchedule(schedule);
		return "redirect:/schedules";
	}
	
	@GetMapping("/edit/{id}")
	public String editform(@PathVariable(name="id")String id, Model model) {
		Schedule schedule = service
				.getScheduleById(id).orElseThrow(()-> new IllegalArgumentException("invalid Id : " + id));
		model.addAttribute("schedule",schedule);
		return "schedule/edit";
	}
	
	@PostMapping("/update/{id}")
	public String editSchedule(@PathVariable(name="id")String id, 
			@ModelAttribute("schedule") Schedule schedule, Model model) {
		service.updateSchedule(id, schedule);
		return "redirect:/schedules";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteSchedule(@PathVariable(name="id")String id) {
		service.deleteSchedule(id);
		return "redirect:/schedules";
	}
	
}
