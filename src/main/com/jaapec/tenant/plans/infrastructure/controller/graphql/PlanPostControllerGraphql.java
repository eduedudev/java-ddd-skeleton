package com.jaapec.tenant.plans.infrastructure.controller.graphql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.application.create.CreatePlanCommand;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.domain.ValueObjects.PlanId;
import com.jaapec.tenant.plans.infrastructure.controller.RequestPlan;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.spring.ApiController;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;

@Controller
public final class PlanPostControllerGraphql extends ApiController {

	private final Validator validator;

	private final PlanRepository repository;
	private final HashMap<String, String> rules = new HashMap<String, String>() {
		{
			put("name", "required|not_empty|max:255|min:3");
			put("description", "required|not_empty|max:255");
			put("priceMonthly", "required|not_empty|double|min:0");
			put("priceYearly", "required|not_empty|double|min:0");
			put("maxUsers", "required|not_empty|integer|min:1");
			put("maxRoles", "required|not_empty|integer|min:1");
			put("maxAccounts", "required|not_empty|integer|min:1");
			put("maxInvoices", "required|not_empty|integer|min:1");
			put("status", "required|not_empty|enum:ACTIVE,INACTIVE");
			put("visibility", "required|not_empty|enum:PUBLIC,PRIVATE");
			put("trialDays", "required|not_empty|integer|min:0");
		}
	};

	public PlanPostControllerGraphql(
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
	public boolean createPlan(@Argument RequestPlan request) throws ValidatorNotExist, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
		ValidationResponse validationResponse = validator.validate(requestJson, rules, repository);
		List<GraphQLCustomException> errors = new ArrayList<>();
		if (validationResponse.hasErrors()) {
			validationResponse
				.errors()
				.forEach((key, value) -> {
					value.forEach(error -> errors.add(new GraphQLCustomException(error, key)));
				});
			throw new GraphQLExceptionList(errors);
		}

		String uuid = UUID.randomUUID().toString();
		dispatch(
			new CreatePlanCommand(
				new PlanId(uuid).value(),
				request.name(),
				request.description(),
				request.priceMonthly(),
				request.priceYearly(),
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
