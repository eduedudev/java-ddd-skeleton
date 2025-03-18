package com.jaapec.tenant.users.infrastructure.controller.reset;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.shared.infrastructure.ApplicationTestCase;

@Transactional
class UserPOSTControllerShould extends ApplicationTestCase {

	@Test
	void not_create_a_user_with_an_existing_id_or_email() throws Exception {
		String requestBody =
			"""
                {
                    "id": "1aab45ba-3c7a-4344-8936-78466eca77fa",
                    "name": "John Doe",
                    "email": "john@doe.com"
                }
                """;
		String expectedResponse =
			"""
                {
                    "code": 400,
                    "data": {
                    "id": [
                        "The id already exists."
                        ]
                    },
                    "error": true
                }
             """;

		create_a_valid_non_existing_user(requestBody);

		assertResponseWithBody("/users", 400, requestBody, expectedResponse);
	}

	private void create_a_valid_non_existing_user(String requestBody) throws Exception {
		assertRequestWithBody("POST", "/users", requestBody, 201);
	}
}
