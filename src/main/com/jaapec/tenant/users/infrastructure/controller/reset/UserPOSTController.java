package com.jaapec.tenant.users.infrastructure.controller.reset;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Logger;
import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.spring.RestApiController;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;
import com.jaapec.tenant.users.application.create.CreateUserCommand;
import com.jaapec.tenant.users.domain.UserRepository;
import com.jaapec.tenant.users.infrastructure.controller.RequestUser;

@RestController
public final class UserPOSTController extends RestApiController {

	UserRepository repository;
	private final Validator validator;
	private final Logger logger;

	@Autowired
	public UserPOSTController(
		QueryBus queryBus,
		CommandBus commandBus,
		UserRepository repository,
		Validator validator,
		Logger logger
	) {
		super(queryBus, commandBus);
		this.repository = repository;
		this.validator = validator;
		this.logger = logger;
	}

	private static final Map<String, String> RULES = Map.of(
		"id", "required|not_empty|uuid",
		"name", "required|not_empty|max:255",
		"email", "required|not_empty|email"
	);

	@PostMapping("/users")
	public ResponseEntity<HashMap<String, Serializable>> createUser(@RequestBody RequestUser request)
		throws IOException, ValidatorNotExist {
		logger.info("Creating user");
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
		ValidationResponse validationResponse = validator.validate(requestJson, RULES, repository);
		HashMap<String, List<String>> validationErrors = new HashMap<>();
		Boolean hasErrors = validationResponse.hasErrors();
		if (Boolean.TRUE.equals(hasErrors)) {
			validationResponse
				.errors()
				.forEach((key, value) -> {
					logger.error(String.format("Validation error: %s", key));
					value.forEach(error -> validationErrors.put(key, new ArrayList<>(List.of(error))));
				});
			return dataResponse(true, ResponseEntity.badRequest(), HttpStatus.BAD_REQUEST.value(), validationErrors);
		}
		dispatch(new CreateUserCommand(request.id(), request.name(), request.email()));
		return dataResponse(false, ResponseEntity.status(201), HttpStatus.CREATED.value(), null);
	}

	@Override
	public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
		return Map.of(ResourceAlreadyExists.class, HttpStatus.BAD_REQUEST);
	}
}
