package com.devsoftec.jaap.users.shared.domain.ValueObjects;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Objects;

public abstract class DateTimeValueObject {

	private final Timestamp value;

	public DateTimeValueObject(String value) {
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

	public static Timestamp currentDateTime() {
		return Timestamp.from(Instant.now());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DateTimeValueObject that = (DateTimeValueObject) o;

		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}
}
