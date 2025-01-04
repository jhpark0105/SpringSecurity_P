package pack.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyUser {
	private int id;
	private String userName;
	private String password;
	private String email; //기타 다양한 변수 선언 가능
	
	private String role; // Authority 정보를 저장할 칼럼 ROLE_USER, ... 이런 형식으로 저장
}
