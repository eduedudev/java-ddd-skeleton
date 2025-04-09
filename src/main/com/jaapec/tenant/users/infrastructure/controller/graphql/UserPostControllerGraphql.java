package com.jaapec.tenant.users.infrastructure.controller.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;
import com.jaapec.tenant.users.application.create.CreateUserCommand;
import com.jaapec.tenant.users.domain.UserRepository;
import com.jaapec.tenant.users.infrastructure.controller.RequestUser;

@Controller
public final class UserPostControllerGraphql extends GraphQLApiController {

	UserRepository repository;
	private final Validator validator;

	public UserPostControllerGraphql(
		QueryBus queryBus,
		CommandBus commandBus,
		UserRepository repository,
		Validator validator
	) {
		super(queryBus, commandBus);
		this.repository = repository;
		this.validator = validator;
	}

	private final Map<String, String> rules = Map.of(
		"id",
		"required|not_empty|uuid",
		"name",
		"required|not_empty|max:255",
		"email",
		"required|not_empty|email"
	);

	@MutationMapping
	public boolean createUser(@Argument RequestUser request) throws JsonProcessingException, ValidatorNotExist {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
		ValidationResponse validationResponse = validator.validate(requestJson, rules, repository);
		List<GraphQLCustomException> errors = new ArrayList<>();
		Boolean hasErrors = validationResponse.hasErrors();
		if (Boolean.TRUE.equals(hasErrors)) {
			validationResponse
				.errors()
				.forEach((key, value) ->
					value.forEach(error -> errors.add(new GraphQLCustomException(error, key)))
				);
			throw new GraphQLExceptionList(errors);
		}
		dispatch(new CreateUserCommand(request.id(), request.name(), request.email()));
		return true;
	}
}
