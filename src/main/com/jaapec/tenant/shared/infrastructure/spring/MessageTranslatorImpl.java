package com.jaapec.tenant.shared.infrastructure.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.jaapec.tenant.shared.domain.MessageTranslator;
import com.jaapec.tenant.shared.domain.Service;

@Service
public final class MessageTranslatorImpl implements MessageTranslator {

	private final MessageSource messageSource;

	@Autowired
	public MessageTranslatorImpl(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String translate(String messageKey, Object[] args) {
		return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
	}
}
