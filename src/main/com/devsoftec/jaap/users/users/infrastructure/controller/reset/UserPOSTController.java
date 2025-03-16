package com.devsoftec.jaap.users.users.infrastructure.controller.reset;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devsoftec.jaap.users.shared.domain.DomainError;
import com.devsoftec.jaap.users.shared.domain.Logger;
import com.devsoftec.jaap.users.shared.domain.ResourceAlreadyExists;
import com.devsoftec.jaap.users.shared.domain.bus.command.CommandBus;
import com.devsoftec.jaap.users.shared.domain.bus.query.QueryBus;
import com.devsoftec.jaap.users.shared.infrastructure.spring.ApiController;
import com.devsoftec.jaap.users.shared.infrastructure.validation.ValidationResponse;
import com.devsoftec.jaap.users.shared.infrastructure.validation.Validator;
import com.devsoftec.jaap.users.shared.infrastructure.validation.ValidatorNotExist;
import com.devsoftec.jaap.users.users.application.create.CreateUserCommand;
import com.devsoftec.jaap.users.users.domain.UserRepository;
import com.devsoftec.jaap.users.users.infrastructure.controller.RequestUser;

@RestController
public final class UserPOSTController extends ApiController {

	UserRepository repository;
	private final Logger logger;

	@Autowired
	public UserPOSTController(QueryBus queryBus, CommandBus commandBus, UserRepository repository, Logger logger) {
		super(queryBus, commandBus);
		this.repository = repository;
		this.logger = logger;
	}

	private final HashMap<String, String> rules = new HashMap<String, String>() {
		{
			put("id", "required|unique|not_empty|uuid");
			put("name", "required|not_empty|max:255");
			put("email", "required|not_empty|email");
		}
	};

	@PostMapping("/users")
	public ResponseEntity<HashMap<String, Serializable>> createUser(@RequestBody RequestUser request)
		throws IOException, ValidatorNotExist {
		logger.info("Creating user");
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
		ValidationResponse validationResponse = Validator.validate(requestJson, rules, repository);
		HashMap<String, List<String>> validationErrors = new HashMap<>();
		if (validationResponse.hasErrors()) {
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
	public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
		return new HashMap<Class<? extends DomainError>, HttpStatus>() {
			{
				put(ResourceAlreadyExists.class, HttpStatus.BAD_REQUEST);
			}
		};
	}
}
