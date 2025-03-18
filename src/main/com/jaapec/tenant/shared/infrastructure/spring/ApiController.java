package com.jaapec.tenant.shared.infrastructure.spring;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.bus.command.Command;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandlerExecutionError;
import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandlerExecutionError;

public abstract class ApiController {

	private final QueryBus queryBus;
	private final CommandBus commandBus;

	public ApiController(QueryBus queryBus, CommandBus commandBus) {
		this.queryBus = queryBus;
		this.commandBus = commandBus;
	}

	protected void dispatch(Command command) throws CommandHandlerExecutionError {
		commandBus.dispatch(command);
	}

	protected <R> R ask(Query query) throws QueryHandlerExecutionError {
		return queryBus.ask(query);
	}

	public abstract HashMap<Class<? extends DomainError>, HttpStatus> errorMapping();

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
}
