package com.jaapec.tenant.shared.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

	private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private DateUtils() {}

	public static String nowAsString() {
		return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
	}

	public static LocalDateTime nowAsDateTime() {
		return LocalDateTime.now();
	}

	public static String format(LocalDateTime dateTime) {
		return dateTime.format(TIMESTAMP_FORMATTER);
	}

	public static LocalDateTime parse(String dateTimeString) {
		return LocalDateTime.parse(dateTimeString, TIMESTAMP_FORMATTER);
	}
}
