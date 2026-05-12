package com.jaapec.tenant.shared.infrastructure.spring;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;

public abstract class RestApiController extends ApiController {

	protected RestApiController(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	protected ResponseEntity<HashMap<String, Serializable>> dataResponse(
		Boolean error,
		ResponseEntity.BodyBuilder status,
		Integer code,
		Object data
	) {
		Jsonb jsonb = JsonbBuilder.create();

		HashMap<String, Serializable> responseMap = new HashMap<>();
		responseMap.put("error", error);
		responseMap.put("code", code);
		String dataJson = jsonb.toJson(data);
		HashMap<String, Serializable> dataMap = jsonb.fromJson(dataJson, HashMap.class);
		responseMap.put("data", dataMap);

		if (code == 201) return ResponseEntity.status(201).build();

		return status.body(responseMap);
	}

	public abstract Map<Class<? extends DomainError>, HttpStatus> errorMapping();
}
