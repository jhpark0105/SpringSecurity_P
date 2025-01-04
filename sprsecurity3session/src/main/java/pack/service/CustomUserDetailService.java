package pack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pack.model.Jikwon;
import pack.repository.JikwonRepository;

@Service
// 사용자 인증 시 사용자 정보를 로드
public class CustomUserDetailService implements UserDetailsService {
   // UserDetailsService : 사용자 정보를 DB 또는 기타 저장소에서 로드 후 UserDetails 객체를 반환
   @Autowired
   private JikwonRepository jikwonRepository;
   
   @Autowired
   private PasswordEncoder passwordEncoder;
   
   @Override
   public UserDetails loadUserByUsername(String sabun) throws UsernameNotFoundException{
      Long jikwonNo = Long.parseLong(sabun);
      Jikwon jikwon = jikwonRepository.findById(jikwonNo)
                  .orElseThrow(() -> new UsernameNotFoundException(sabun +"에 직원이 없어요"));
      return User.builder()
            .username(jikwon.getJikwonname())
            .password(passwordEncoder.encode(jikwon.getJikwonname()))
          //가급적이면 pk는 password로 사용하지 않음
            .build();
   }
}