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
		if (isInvalidTenant(tenant)) {
			return false;
		}

		try {
			Record[] records = getCnameRecords(tenant.domain().value());

			if (records == null || records.length == 0) {
				return false;
			}

			return isExpectedCnameFound(records);
		} catch (TextParseException e) {
			throw new RuntimeException("Error occurred during DNS lookup", e);
		}
	}

	private boolean isInvalidTenant(Tenant tenant) {
		return tenant == null || tenant.domain() == null || tenant.domain().value() == null;
	}

	private Record[] getCnameRecords(String domain) throws TextParseException {
		Lookup lookup = new Lookup(domain, Type.CNAME);
		return lookup.run();
	}

	private boolean isExpectedCnameFound(Record[] records) {
		for (Record record : records) {
			if (record instanceof CNAMERecord cname) {
				if (EXPECTED_CNAME.equalsIgnoreCase(cname.getTarget().toString())) {
					return true;
				}
			}
		}
		return false;
	}
}
