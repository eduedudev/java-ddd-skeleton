package com.jaapec.tenant.shared.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;

import com.jaapec.tenant.shared.domain.Service;

@Service
public final class Parameter {

	private final Dotenv dotenv;

	public Parameter(Dotenv dotenv) {
		this.dotenv = dotenv;
	}

	public String get(String key) throws ParameterNotExist {
		String value = System.getenv(key); // <-- First, look in environment (K8s)

		if (value == null) {
			value = dotenv.get(key); // <-- Then .env (only local dev)
		}

		if (null == value) {
			throw new ParameterNotExist(key);
		}

		return value;
	}

	public Integer getInt(String key) throws ParameterNotExist {
		String value = get(key);

		return Integer.parseInt(value);
	}
}
