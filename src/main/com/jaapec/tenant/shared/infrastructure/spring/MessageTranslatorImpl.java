package com.jaapec.tenant.shared.infrastructure.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.jaapec.tenant.shared.domain.MessageTranslator;
import com.jaapec.tenant.shared.domain.Service;

@Service
public class MessageTranslatorImpl implements MessageTranslator {

	@Autowired
	private MessageSource messageSource;

	@Override
	public String translate(String messageKey, Object[] args) {
		return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
	}
}
