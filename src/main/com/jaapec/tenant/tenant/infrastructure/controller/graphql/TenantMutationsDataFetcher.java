package com.jaapec.tenant.tenant.infrastructure.controller.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
import com.jaapec.tenant.tenant.application.cancel_auto_renew.CancelAutoRenewCommand;
import com.jaapec.tenant.tenant.application.change_domain.ChangeTenantDomainCommand;
import com.jaapec.tenant.tenant.application.create.CreateTenantCommand;
import com.jaapec.tenant.tenant.application.update.UpdateTenantCommand;
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
	public boolean createTenant(@Argument RequestTenant request) throws ValidatorNotExist {
		checkTenant(request);
		String uuid = UUID.randomUUID().toString();
		String ownerId = new TenantId(uuid).value();
		dispatch(new CreateTenantCommand(uuid, request.name(), ownerId));
		return true;
	}

	@MutationMapping
	public boolean updateTenant(@Argument String id, @Argument RequestTenant request) throws ValidatorNotExist {
		checkTenant(request);
		dispatch(new UpdateTenantCommand(id, request.name()));
		return true;
	}

	@MutationMapping
	public boolean changeDomain(@Argument String id, @Argument RequestDomain request) {
		dispatch(new ChangeTenantDomainCommand(id, request.domain()));
		return true;
	}

	@MutationMapping
	public boolean cancelAutoRenew(@Argument String tenantId, @Argument String subscriptionId) {
		dispatch(new CancelAutoRenewCommand(tenantId, subscriptionId));
		return true;
	}

	private void checkTenant(RequestTenant request) throws ValidatorNotExist {
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
	}
}
