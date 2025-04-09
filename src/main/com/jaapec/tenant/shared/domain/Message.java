package com.jaapec.tenant.shared.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public record Message(String messageKey, Object[] args) implements Serializable {
	public Message {
		if (messageKey == null || messageKey.isBlank()) {
			throw new IllegalArgumentException("Message key must not be null or blank.");
		}
		args = args != null ? args : new Object[0];
	}

	public String formattedMessage(MessageTranslator translator) {
		return translator.translate(messageKey, args);
	}

	@Override
	public String toString() {
		return "Message{" +
			"messageKey='" + messageKey + '\'' +
			", args=" + Arrays.toString(args) +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;
		return Arrays.equals(args, message.args) && Objects.equals(messageKey, message.messageKey);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(messageKey);
		result = 31 * result + Arrays.hashCode(args);
		return result;
	}
}
