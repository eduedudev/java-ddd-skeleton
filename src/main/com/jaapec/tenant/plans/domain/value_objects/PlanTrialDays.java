package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class PlanTrialDays extends IntValueObject {

	private static final int MIN_TRIAL_DAYS = 0;

	public PlanTrialDays(Integer value) {
		super(ensureValidTrialDays(value));
	}

	public PlanTrialDays() {
		super(null);
	}

	private static Integer ensureValidTrialDays(Integer value) {
		if (value.compareTo(0) < MIN_TRIAL_DAYS) throw new MinValueException("TrialDays", value.toString());
		return value;
	}
}
