package pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	public void printAllCustomers() { // 전체 자료 출력
		customerRepository.findAll().forEach(customer -> {
			System.out.println(customer);
		});
	}
	
	//추가
	public void insCustomer(String name, int age, String gender) {
		Customer exCustomer = customerRepository.findByName(name);
		if(exCustomer == null) { //동일 고객명이 없는 경우만 추가
			Customer newCustomer = new Customer();
			newCustomer.setName(name);
			newCustomer.setAge(age);
			newCustomer.setGender(gender);
			customerRepository.save(newCustomer);
			
			System.out.println("새 고객 추가 성공!" + newCustomer);
		} else {
			System.out.println("동일 고객명이 있어요. 추가 불가능한~");
		}
	}
	
	//수정
	public void upCustomer(String name) {
		Customer exCustomer = customerRepository.findByName(name);
		if(exCustomer != null) { //나이, 성별 수정
			exCustomer.setAge(33);
			exCustomer.setGender("여성");
			customerRepository.save(exCustomer);
			
			System.out.println("고객 수정 성공!" + exCustomer);
		} else {
			System.out.println("고객 찾기 실패");
		}
	}
	
	//삭제
	public void delCustomer(String name) {
		Customer exCustomer = customerRepository.findByName(name);
		if(exCustomer != null) { 
			customerRepository.delete(exCustomer);
				
			System.out.println("고객 삭제 성공!" + exCustomer);
		} else {
			System.out.println("고객 찾기 실패");
		}
	}
}
