package com.jaapec.tenant.shared.infrastructure.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.jaapec.tenant.shared.domain.MessageTranslator;

@Configuration
public class ServerConfiguration {

	private final RequestMappingHandlerMapping mapping;
	private final MessageTranslator translator;

	public ServerConfiguration(
		@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping,
		MessageTranslator translator
	) {
		this.mapping = mapping;
		this.translator = translator;
	}

	@Bean
	public FilterRegistrationBean<ApiExceptionMiddleware> apiExceptionMiddleware() {
		FilterRegistrationBean<ApiExceptionMiddleware> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new ApiExceptionMiddleware(mapping, translator));

		return registrationBean;
	}
}
