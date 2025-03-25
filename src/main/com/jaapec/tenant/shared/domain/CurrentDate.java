package com.jaapec.tenant.shared.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface CurrentDate {
	static String now() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
	}
}
