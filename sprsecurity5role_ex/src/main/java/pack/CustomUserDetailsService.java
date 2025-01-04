package pack;

import java.beans.Encoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pack.entity.MyUser;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	//원래는 DB에서 사용자 정보를 읽어야함,... 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//사용자의 정보를 읽어 UserDetails 타입으로 반환
		System.out.println("username : " + username);
		
		String role = "";
		if(username.equals("guest")) {
			role="ROLE_USER";
		} else if(username.equals("batman")) {
			role="ROLE_STAFF";
		} else if(username.equals("superman")) {
			role="ROLE_ADMIN";
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		MyUser myUser = MyUser.builder()
								.id(1)
								.userName(username)
								.password(encoder.encode("1234"))
								.role(role)
								.build();
		
		//GrantedAuthority 인터페이스 : 사용자가 어떤 권한을 가지고 있는지를 나타내기 위해 사용함
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new SimpleGrantedAuthority(myUser.getRole()));
		
		UserDetails userDetails = new User(myUser.getUserName(), myUser.getPassword(), authList);
		
		return userDetails; //얘를 이용해 스프링 시큐리티가 계정과 비밀번호 유효성 검증을 하고 권한도 검증한다
	}
}
