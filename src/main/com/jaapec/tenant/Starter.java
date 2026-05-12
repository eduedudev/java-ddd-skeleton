package com.jaapec.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jaapec.tenant.shared.domain.Service;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class })
@ComponentScan(
	includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class),
	value = { "com.jaapec.tenant" }
)
@EnableScheduling
public class Starter {

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
