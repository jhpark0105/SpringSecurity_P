package pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/alldata")
	public String alldata() {
		customerService.printAllCustomers();
		
		return "전체 자료 출력";
	}
	
	//클라이언트에서 자료 받기 위해 전부 getmapping 사용
	//@PostMapping("/insdata")
	@GetMapping("/insdata")
	public String insdata(@RequestParam(name="name")String name,
			@RequestParam(name="age")int age,
			@RequestParam(name="gender")String gender) {
		customerService.insCustomer(name, age, gender);
		return "추가 완료";
	}
	
	//@PutMapping("/updata")
	@GetMapping("/updata")
	public String updata(@RequestParam(name="name")String name) {
		customerService.upCustomer(name);
		return "수정 완료";
	}
	
	//@DeleteMapping("/deldata")
	@GetMapping("/deldata")
	public String deldata(@RequestParam(name="name")String name) {
		customerService.delCustomer(name);
		return "삭제 완료";
	}
}
