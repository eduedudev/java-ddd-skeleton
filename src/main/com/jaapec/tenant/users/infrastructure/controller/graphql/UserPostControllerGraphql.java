package com.jaapec.tenant.users.infrastructure.controller.graphql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.spring.ApiController;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;
import com.jaapec.tenant.users.application.create.CreateUserCommand;
import com.jaapec.tenant.users.domain.UserRepository;
import com.jaapec.tenant.users.infrastructure.controller.RequestUser;

@Controller
public final class UserPostControllerGraphql extends ApiController {

	UserRepository repository;

	public UserPostControllerGraphql(QueryBus queryBus, CommandBus commandBus, UserRepository repository) {
		super(queryBus, commandBus);
		this.repository = repository;
	}

	private final HashMap<String, String> rules = new HashMap<String, String>() {
		{
			put("id", "required|unique|not_empty|uuid");
			put("name", "required|not_empty|max:255");
			put("email", "required|not_empty|email");
		}
	};

	@MutationMapping
	public boolean createUser(@Argument RequestUser request) throws JsonProcessingException, ValidatorNotExist {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
		ValidationResponse validationResponse = Validator.validate(requestJson, rules, repository);
		List<GraphQLCustomException> errors = new ArrayList<>();
		if (validationResponse.hasErrors()) {
			validationResponse
				.errors()
				.forEach((key, value) -> {
					value.forEach(error -> errors.add(new GraphQLCustomException(error, key)));
				});
			throw new GraphQLExceptionList(errors);
		}
		dispatch(new CreateUserCommand(request.id(), request.name(), request.email()));
		return true;
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
