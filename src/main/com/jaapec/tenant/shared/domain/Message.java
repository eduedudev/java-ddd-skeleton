package com.jaapec.tenant.shared.domain;

public record Message(String messageKey, Object[] args) {

	public Message {
		if (messageKey == null || messageKey.isBlank()) {
			throw new IllegalArgumentException("Message key must not be null or blank.");
		}
		args = args != null ? args : new Object[0];
	}

	public String formattedMessage(MessageTranslator translator) {
		return translator.translate(messageKey, args);
	}
}
