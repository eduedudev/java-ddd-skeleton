package com.jaapec.tenant.plan.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jaapec.tenant.plan.domain.PlanMother;
import com.jaapec.tenant.plans.application.PlanResponse;
import com.jaapec.tenant.plans.domain.Plan;

public final class PlansResponseMother {

	public static List<PlanResponse> create(List<Plan> plans) {
		return plans.stream().map(PlanResponseMother::create).toList();
	}

	public static List<PlanResponse> random() {
		int size = new Random().nextInt(100);
		List<Plan> plans = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			plans.add(PlanMother.random());
		}
		return create(plans);
	}
}
