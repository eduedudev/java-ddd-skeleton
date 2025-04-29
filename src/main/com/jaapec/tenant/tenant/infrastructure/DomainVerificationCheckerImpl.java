package com.jaapec.tenant.tenant.infrastructure;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.tenant.domain.DomainVerificationChecker;
import com.jaapec.tenant.tenant.domain.Tenant;

@Service
public final class DomainVerificationCheckerImpl implements DomainVerificationChecker {

	private static final String EXPECTED_CNAME = "custom.jaapec.com";

	@Override
	public boolean isVerified(Tenant tenant) {
		if (tenant == null || tenant.domain() == null || tenant.domain().value() == null) {
			return false;
		}

		try {
			Lookup lookup = new Lookup(tenant.domain().value(), Type.CNAME);
			Record[] records = lookup.run();

			if (records == null) return false;

			for (Record record : records) {
				if (record instanceof CNAMERecord cname) {
					if (EXPECTED_CNAME.equalsIgnoreCase(cname.getTarget().toString())) {
						return true;
					}
				}
			}
		} catch (TextParseException e) {
			// TODO: poner un log aqui
			throw new RuntimeException(e);
		}

		return false;
	}
}
