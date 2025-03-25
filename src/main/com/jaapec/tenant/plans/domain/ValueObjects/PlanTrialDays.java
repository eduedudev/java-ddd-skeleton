package com.jaapec.tenant.plans.domain.ValueObjects;

import com.jaapec.tenant.plans.domain.MinValueException;
import com.jaapec.tenant.shared.domain.ValueObjects.IntValueObject;

public final class PlanTrialDays extends IntValueObject {

	private static final int minTrialDays = 0;

	public PlanTrialDays(Integer value) {
		super(ensureValidTrialDays(value));
	}

	public PlanTrialDays() {
		super(null);
	}

	private static Integer ensureValidTrialDays(Integer value) {
		if (value.compareTo(0) < minTrialDays) throw new MinValueException("TrialDays", value.toString());
		return value;
	}
}
