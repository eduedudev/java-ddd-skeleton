package com.devsoftec.jaap.users.shared.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class InfrastructureTestCase {

	protected void eventually(Runnable assertion) throws Exception {
		int attempts = 0;
		boolean allOk = false;

		int MAX_ATTEMPTS = 3;
		while (attempts < MAX_ATTEMPTS && !allOk) {
			try {
				assertion.run();

				allOk = true;
			} catch (Throwable error) {
				attempts++;

				int MILLIS_TO_WAIT_BETWEEN_RETRIES = 300;
				Thread.sleep(MILLIS_TO_WAIT_BETWEEN_RETRIES);
			}
		}
	}
}
