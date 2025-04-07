package com.jaapec.tenant.shared.domain.value_objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class DateValueObject {

	private final Date value;

	protected DateValueObject(String value) {
		this.value = convertToDate(value);
	}

	public String value() {
		return (value != null) ? convertToString(value) : null;
	}

	public static Date convertToDate(String dateTimeString) {
		if (dateTimeString == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(dateTimeString);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String convertToString(Date dateTime) {
		if (dateTime == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(dateTime);
	}

	@Override
	public String toString() {
		return "DateValueObject{" + "value=" + value + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		DateValueObject that = (DateValueObject) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
