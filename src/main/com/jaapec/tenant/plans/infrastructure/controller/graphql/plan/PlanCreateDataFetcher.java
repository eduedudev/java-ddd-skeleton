package com.jaapec.tenant.plans.infrastructure.controller.graphql.plan;

import java.util.*;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.value_objects.PlanId;
import com.jaapec.tenant.plans.infrastructure.controller.RequestPlan;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;

@Controller
public final class PlanCreateDataFetcher extends GraphQLApiController {

	private final Validator validator;

	private final PlanRepository repository;
	private final Map<String, String> rules = Map.ofEntries(
		Map.entry("name", "required|not_empty|max:255|min:3"),
		Map.entry("description", "required|not_empty|max:255"),
		Map.entry("maxUsers", "required|not_empty|integer|min:1"),
		Map.entry("maxRoles", "required|not_empty|integer|min:1"),
		Map.entry("maxAccounts", "required|not_empty|integer|min:1"),
		Map.entry("maxInvoices", "required|not_empty|integer|min:1"),
		Map.entry("status", "required|not_empty|enum:ACTIVE,INACTIVE"),
		Map.entry("visibility", "required|not_empty|enum:PUBLIC,PRIVATE"),
		Map.entry("trialDays", "required|not_empty|integer|min:0")
	);

	public PlanCreateDataFetcher(
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
	public boolean createPlan(@Argument RequestPlan request) throws ValidatorNotExist {
		Jsonb jsonb = JsonbBuilder.create();
		String requestJson = jsonb.toJson(request);
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
		dispatch(
			new CreatePlanCommand(
				new PlanId(uuid).value(),
				request.name(),
				request.description(),
				request.maxUsers(),
				request.maxRoles(),
				request.maxAccounts(),
				request.maxInvoices(),
				request.status(),
				request.visibility(),
				request.trialDays()
			)
		);
		return true;
	}
}
