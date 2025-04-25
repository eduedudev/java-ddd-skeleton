package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;
import com.jaapec.tenant.tenant.application.create.CreateTenantCommand;
import com.jaapec.tenant.tenant.domain.TenantId;

@Controller
public final class TenantMutationsDataFetcher extends GraphQLApiController {

	private final Validator validator;

	private final PlanRepository repository;
	private final Map<String, String> rules = Map.ofEntries(Map.entry("name", "required|not_empty|max:255|min:3"));

	public TenantMutationsDataFetcher(
		QueryBus queryBus,
		CommandBus commandBus,
		Validator validator,
		PlanRepository repository
	) {
		super(queryBus, commandBus);
		this.validator = validator;
		this.repository = repository;
	}

	@MutationMapping
	public boolean createTenant(@Argument RequestTenant request) throws ValidatorNotExist, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
		ValidationResponse validationResponse = validator.validate(requestJson, rules, repository);
		List<GraphQLCustomException> errors = new ArrayList<>();
		Boolean hasErrors = validationResponse.hasErrors();
		if (Boolean.TRUE.equals(hasErrors)) {
			validationResponse
				.errors()
				.forEach((key, value) -> value.forEach(error -> errors.add(new GraphQLCustomException(error, key))));
			throw new GraphQLExceptionList(errors);
		}

		String uuid = UUID.randomUUID().toString();
		// TODO: Replace mock-owner-id with actual ownerId from authenticated user (JWT claim "sub")
		String ownerId = new TenantId(uuid).value();
		dispatch(new CreateTenantCommand(uuid, request.name(), ownerId));
		return true;
	}
}
