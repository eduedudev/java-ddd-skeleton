package com.devsoftec.jaap.users.shared.infrastructure;

import java.util.UUID;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.UuidGenerator;

@Service
public final class JavaUuidGenerator implements UuidGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
