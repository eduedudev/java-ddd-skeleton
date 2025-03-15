package com.devsoftec.jaap.users.users.infrastructure.controller.grapghql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.transaction.Transactional;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import com.devsoftec.jaap.users.shared.infrastructure.ApplicationTestCase;

@Transactional
class UserPostControllerGraphqlShould extends ApplicationTestCase {

	@Test
	void not_create_a_user_with_an_existing_id_or_email() throws Exception {
		@Language("GraphQL")
		String mutation =
			"mutation CreateUser($id: ID!, $name: String!, $email: String!) {" +
			" createUser(request: {id: $id, name: $name, email: $email})" +
			"}";

		@Language("JSONPath")
		String jsonPath = "$.data.createUser";

		Map<String, Object> variables = Map.of(
			"id",
			"1aab45ba-3c7a-4344-8936-78466eca77fa",
			"name",
			"John Doe",
			"email",
			"john@doe.com"
		);

		create_a_valid_non_existing_user(mutation, jsonPath, variables);

		assertResponseWithBody(mutation, jsonPath, variables, "The id already exists.");
	}

	private void create_a_valid_non_existing_user(
		@Language("GraphQL") String mutation,
		@Language("JSONPath") String jsonPath,
		Map<String, Object> variables
	) throws Exception {
		assertResponse(mutation, jsonPath, variables);
	}
}
