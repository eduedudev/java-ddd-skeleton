package com.jaapec.tenant.plan.application.change_visibility;

import com.jaapec.tenant.plans.application.change_visibility.ChangeVisibilityPlanCommand;

public final class ChangeVisibilityPlanCommandMother {

	public static ChangeVisibilityPlanCommand create(String id, String visibility) {
		return new ChangeVisibilityPlanCommand(id, visibility);
	}
}
