package com.jaapec.tenant.plan.application.add_price;

import com.jaapec.tenant.plan.domain.AmountMother;
import com.jaapec.tenant.plan.domain.BillingIntervalMother;
import com.jaapec.tenant.plan.domain.CurrencyMother;
import com.jaapec.tenant.plan.domain.PlanPriceIdMother;
import com.jaapec.tenant.plans.application.add_price.AddPlanPriceCommand;
import com.jaapec.tenant.plans.domain.value_objects.*;

public final class AddPlanPriceCommandMother {

	public static AddPlanPriceCommand create(
		PlanId planId,
		PlanPriceId id,
		BillingInterval billingInterval,
		Amount amount,
		Currency currency
	) {
		return new AddPlanPriceCommand(
			planId.value(),
			id.value(),
			billingInterval.value(),
			amount.value(),
			currency.value()
		);
	}

	public static AddPlanPriceCommand random(PlanId planId) {
		return create(
			planId,
			PlanPriceIdMother.random(),
			BillingIntervalMother.random(),
			AmountMother.random(),
			CurrencyMother.random()
		);
	}
}
