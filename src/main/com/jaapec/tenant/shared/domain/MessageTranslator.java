package com.jaapec.tenant.shared.domain;

public interface MessageTranslator {
	String translate(String messageKey, Object[] args);
}
