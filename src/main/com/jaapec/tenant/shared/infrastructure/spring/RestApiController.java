package com.jaapec.tenant.shared.infrastructure.spring;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;

public abstract class RestApiController extends ApiController {

	public RestApiController(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	protected ResponseEntity<HashMap<String, Serializable>> dataResponse(
		Boolean error,
		ResponseEntity.BodyBuilder status,
		Integer code,
		Object data
	) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		HashMap<String, Serializable> responseMap = new HashMap<>();
		responseMap.put("error", error);
		responseMap.put("code", code);
		HashMap<String, Serializable> dataMap = objectMapper.readValue(
			objectMapper.writeValueAsString(data),
			HashMap.class
		);
		responseMap.put("data", dataMap);

		if (code == 201) return ResponseEntity.status(201).build();

		return status.body(responseMap);
	}

	public abstract Map<Class<? extends DomainError>, HttpStatus> errorMapping();
}
