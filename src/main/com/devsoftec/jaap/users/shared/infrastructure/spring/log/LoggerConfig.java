package com.devsoftec.jaap.users.shared.infrastructure.spring.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsoftec.jaap.users.shared.domain.Logger;

@Configuration
public class LoggerConfig {

	@Bean
	public Logger logger() {
		return new Log4j2Logger();
	}
}
