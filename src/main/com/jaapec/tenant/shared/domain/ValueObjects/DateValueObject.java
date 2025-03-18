package com.jaapec.tenant.shared.domain.ValueObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateValueObject {

	private final Date value;

	public DateValueObject(String value) {
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DateValueObject)) {
			return false;
		}
		DateValueObject that = (DateValueObject) o;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
