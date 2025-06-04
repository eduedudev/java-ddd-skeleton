package com.jaapec.tenant.shared.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfig {

	@Bean
	public Dotenv dotenv() {
		try {
			return Dotenv.configure().directory("/").filename(".env").load();
		} catch (Exception e) {
			return null;
		}
	}
}
