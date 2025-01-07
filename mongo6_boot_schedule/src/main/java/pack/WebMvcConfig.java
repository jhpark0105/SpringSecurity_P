package pack;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	//custom formatter나 변환기를 등록할 수 있다.
	@Override
	public void addFormatters(FormatterRegistry registry) {
		//DateTimeFormatterRegistrar : 날짜와 시간에 대한 형식 설정을 관리
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		
		// 날짜와 시간을 "yyyy-mm-dd HH:mm:ss" 형식으로 변환하기 위한 ISO 표준 값 중 하나
		// "2025-01-07 T10:09:15"
		registrar.setDateTimeFormatter(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		registrar.registerFormatters(registry);
	}
}
