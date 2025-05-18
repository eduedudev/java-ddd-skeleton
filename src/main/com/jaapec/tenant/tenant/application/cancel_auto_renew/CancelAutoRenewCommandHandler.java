package com.jaapec.tenant.tenant.application.cancel_auto_renew;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;
import com.jaapec.tenant.subscription.domain.SubscriptionId;
import com.jaapec.tenant.tenant.domain.TenantId;

@Service
public final class CancelAutoRenewCommandHandler implements CommandHandler<CancelAutoRenewCommand> {

	private final SubscriptionAutoRenewCanceler canceler;

	public CancelAutoRenewCommandHandler(SubscriptionAutoRenewCanceler canceler) {
		this.canceler = canceler;
	}

	@Override
	public void handle(CancelAutoRenewCommand command) {
		canceler.cancel(new TenantId(command.tenantId()), new SubscriptionId(command.subscriptionId()));
	}
}
