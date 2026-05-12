package com.jaapec.tenant.plans.infrastructure.controller.graphql.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.plans.application.add_price.AddPlanPriceCommand;
import com.jaapec.tenant.plans.domain.PlanRepository;
import com.jaapec.tenant.plans.infrastructure.controller.RequestPlanPrice;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;

@Controller
public final class PlanAddPriceDataFetcher extends GraphQLApiController {

	private final Validator validator;

	private final PlanRepository repository;
	private final Map<String, String> rules = Map.ofEntries(
		Map.entry("billingInterval", "required|not_empty|enum:MONTHLY,YEARLY"),
		Map.entry("amount", "required|not_empty|integer|min:0"),
		Map.entry("currency", "required|not_empty|enum:USD")
	);
	private final Map<String, String> rules2 = Map.of("id", "required|not_empty|uuid");

	public PlanAddPriceDataFetcher(
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
	public boolean addPlanPrice(@Argument String id, @Argument RequestPlanPrice request) throws ValidatorNotExist {
		Jsonb jsonb = JsonbBuilder.create();
		String requestJson = jsonb.toJson(request);
		ValidationResponse validationResponse = validator.validate(requestJson, rules, repository);
		validationResponse.addError(validator.validate("{\"id\":\"" + id + "\"}", rules2, repository).errors());
		List<GraphQLCustomException> errors = new ArrayList<>();
		Boolean hasErrors = validationResponse.hasErrors();
		if (Boolean.TRUE.equals(hasErrors)) {
			validationResponse
				.errors()
				.forEach((key, value) -> value.forEach(error -> errors.add(new GraphQLCustomException(error, key))));
			throw new GraphQLExceptionList(errors);
		}
		String uuid = UUID.randomUUID().toString();

		dispatch(new AddPlanPriceCommand(id, uuid, request.billingInterval(), request.amount(), request.currency()));
		return true;
	}
}
