package com.jaapec.tenant.shared.infrastructure.spring.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jaapec.tenant.shared.domain.Logger;

@Configuration
public class LoggerConfig {

	@Bean
	public Logger logger() {
		return new Log4j2Logger();
	}
}
