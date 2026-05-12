package com.jaapec.tenant.shared.domain.value_objects;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class DateTimeValueObject {

	private final Timestamp value;

	protected DateTimeValueObject(String value) {
		if (value == null) {
			throw new IllegalArgumentException("DateTime value cannot be null");
		}
		this.value = convertToDateTime(value);
	}

	public String value() {
		return (value != null) ? convertToString(value) : null;
	}

	public static Timestamp convertToDateTime(String dateTimeString) {
		if (dateTimeString == null) {
			return null;
		}
		return Timestamp.valueOf(dateTimeString);
	}

	public static String convertToString(Timestamp dateTime) {
		if (dateTime == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
		return dateFormat.format(dateTime);
	}

	public LocalDateTime valueAsDateTime() {
		return (value != null) ? value.toLocalDateTime() : null;
	}

	public static Timestamp currentDateTime() {
		return Timestamp.from(Instant.now());
	}

	@Override
	public String toString() {
		return "DateTimeValueObject{" + "value=" + value + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		DateTimeValueObject that = (DateTimeValueObject) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
