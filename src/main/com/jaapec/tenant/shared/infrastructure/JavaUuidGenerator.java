package com.jaapec.tenant.shared.infrastructure;

import java.util.UUID;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.UuidGenerator;

@Service
public final class JavaUuidGenerator implements UuidGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
