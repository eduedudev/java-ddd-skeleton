package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import java.util.*;

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
import com.jaapec.tenant.subscription.domain.SubscriptionSource;
import com.jaapec.tenant.tenant.application.add_subscription.AddSubscriptionCommand;
import com.jaapec.tenant.tenant.domain.TenantRepository;

@Controller
public final class AddTenantSubscription extends GraphQLApiController {

	private final Validator validator;
	private final TenantRepository repository;

	private final Map<String, String> rules = Map.ofEntries(
		Map.entry("planId", "required|not_empty|uuid"),
		Map.entry("billingInterval", "required|not_empty|enum:MONTHLY,YEARLY"),
		Map.entry("pricing", "required|not_empty|integer|min:0"),
		Map.entry("currency", "required|not_empty|enum:USD"),
		Map.entry("coupon", "not_empty|max:10"),
		Map.entry("autoRenew", "required|not_empty")
	);
	private final Map<String, String> rules2 = Map.of("id", "required|not_empty|uuid");

	public AddTenantSubscription(
		QueryBus queryBus,
		CommandBus commandBus,
		Validator validator,
		TenantRepository repository
	) {
		super(queryBus, commandBus);
		this.validator = validator;
		this.repository = repository;
	}

	@MutationMapping
	public boolean addSubscription(@Argument String id, @Argument RequestSubscription request)
		throws ValidatorNotExist, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(request);
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
		dispatch(
			new AddSubscriptionCommand(
				uuid,
				id,
				request.planId(),
				request.billingInterval(),
				request.pricing(),
				request.currency(),
				request.coupon(),
				SubscriptionSource.source.BACKOFFICE.toString(),
				request.autoRenew()
			)
		);
		return true;
	}
}
