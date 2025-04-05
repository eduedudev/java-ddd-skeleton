package com.jaapec.tenant.shared.infrastructure;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Map;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApplicationTestCase {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EventBus eventBus;

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	/**
	 * Verifies that an HTTP GET request to the given endpoint returns the expected status code
	 * and, if applicable, the expected response content.
	 *
	 * @param endpoint the API endpoint to be tested.
	 * @param expectedStatusCode the HTTP status code expected as a result of the request.
	 * @param expectedResponse the expected response content as a string. If empty, it asserts the response is empty.
	 * @throws Exception if any exception occurs during the execution of the test.
	 */
	protected void assertResponse(String endpoint, Integer expectedStatusCode, String expectedResponse) throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

		mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatusCode)).andExpect(response);
	}

	/**
	 * Asserts the response of a GraphQL mutation based on a given JSONPath and a set of variables.
	 * Executes a GraphQL mutation, extracts the JSON response at a specified JSONPath,
	 * and validates the extracted result.
	 *
	 * @param graphql the GraphQL mutation to execute.
	 * @param jsonPath the JSONPath expression used to extract a specific part of the response.
	 * @param variables a map of variables required for the mutation.
	 * @throws Exception if the execution of the GraphQL mutation fails or the assertion fails.
	 */
	protected void assertResponse(
		@Language("GraphQL") String graphql,
		@Language("JSONPath") String jsonPath,
		Map<String, Object> variables
	) throws Exception {
		Boolean result = dgsQueryExecutor.executeAndExtractJsonPath(graphql, jsonPath, variables);
		assertTrue(result);
	}

	/**
	 * Executes a GraphQL query and extracts a specific part of the JSON response
	 * using the provided JSONPath expression. The extracted result is then mapped
	 * to the specified type.
	 *
	 * @param graphql the GraphQL query to execute.
	 * @param jsonPath the JSONPath expression used to extract a specific part of the response.
	 * @param variables a map of variables to be passed to the query.
	 * @param assertions the class representing the type to map the extracted response to.
	 * @return the extracted and mapped response as an object of the specified type.
	 * @throws Exception if there is an error during query execution or response mapping.
	 */
	protected <T> T assertResponseWithBody(
		@Language("GraphQL") String graphql,
		@Language("JSONPath") String jsonPath,
		Map<String, Object> variables,
		Class<T> assertions
	) throws Exception {
		return dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphql, jsonPath, variables, assertions);
	}

	/**
	 * Asserts that a GraphQL query execution results in an error response with the expected message
	 * and extensions.
	 *
	 * @param graphql the GraphQL query to be executed.
	 * @param variables a map of variables to be passed to the query.
	 * @param expectedMessage the expected error message.
	 * @param expectedExtensions a map of expected extensions to be included in the error response.
	 * @throws Exception if the assertion fails or any unexpected error occurs during query execution.
	 */
	protected void assertErrorResponse(
		@Language("GraphQL") String graphql,
		Map<String, Object> variables,
		String expectedMessage,
		Map<String, Object> expectedExtensions
	) throws Exception {
		ExecutionResult result = dgsQueryExecutor.execute(graphql, variables);

		assertFalse(result.getErrors().isEmpty());
		GraphQLError error = result.getErrors().getFirst();

		assertEquals(expectedMessage, error.getMessage());
		assertThat(error.getExtensions()).containsAllEntriesOf(expectedExtensions);
	}

	/**
	 * Verifies that an HTTP GET request to the specified endpoint returns the expected status code
	 * and matches the expected response content or asserts the content is empty if applicable.
	 *
	 * @param endpoint the API endpoint to be tested.
	 * @param expectedStatusCode the HTTP status code expected as the result of the request.
	 * @param expectedResponse the expected response content as a string. If empty, it asserts the response is empty.
	 * @param headers the HTTP headers to include in the request.
	 * @throws Exception if any exception occurs during the request execution or assertion process.
	 */
	protected void assertResponse(
		String endpoint,
		Integer expectedStatusCode,
		String expectedResponse,
		HttpHeaders headers
	) throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

		mockMvc.perform(get(endpoint).headers(headers)).andExpect(status().is(expectedStatusCode)).andExpect(response);
	}

	/**
	 * Asserts that an HTTP POST request to the specified endpoint with the given request body
	 * results in the expected status code and optionally matches the expected response content.
	 *
	 * @param endpoint the API endpoint to which the HTTP POST request is sent.
	 * @param expectedStatusCode the HTTP status code expected as a result of the request.
	 * @param body the request body to be sent with the HTTP POST request.
	 * @param expectedResponse the expected response content as a string. If empty, it asserts the response is empty.
	 * @throws Exception if any error occurs during the request execution or assertion process.
	 */
	protected void assertResponseWithBody(
		String endpoint,
		Integer expectedStatusCode,
		String body,
		String expectedResponse
	) throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);
		mockMvc
			.perform(request(HttpMethod.POST, endpoint).content(body).contentType(APPLICATION_JSON))
			.andExpect(status().is(expectedStatusCode))
			.andExpect(response);
	}

	/**
	 * Asserts that an HTTP request to a specified endpoint with the given method and request body
	 * results in the expected HTTP status code. The request is sent with a JSON content type,
	 * and the response body is expected to be empty.
	 *
	 * @param method the HTTP method to be used for the request (e.g., "POST", "PUT").
	 * @param endpoint the API endpoint to which the request is sent.
	 * @param body the JSON body content to include with the request.
	 * @param expectedStatusCode the HTTP status code expected as the response.
	 * @throws Exception if any exception occurs during the execution or verification of the request.
	 */
	protected void assertRequestWithBody(String method, String endpoint, String body, Integer expectedStatusCode)
		throws Exception {
		mockMvc
			.perform(request(HttpMethod.valueOf(method), endpoint).content(body).contentType(APPLICATION_JSON))
			.andExpect(status().is(expectedStatusCode))
			.andExpect(content().string(""));
	}

	/**
	 * Verifies that an HTTP request to a specified endpoint with the provided method
	 * results in the expected HTTP status code and an empty response body.
	 *
	 * @param method the HTTP method to be used for the request (e.g., "GET", "POST").
	 * @param endpoint the API endpoint to which the request is sent.
	 * @param expectedStatusCode the HTTP status code expected as the response.
	 * @throws Exception if any exception occurs during the execution or verification of the request.
	 */
	protected void assertRequest(String method, String endpoint, Integer expectedStatusCode) throws Exception {
		mockMvc
			.perform(request(HttpMethod.valueOf(method), endpoint))
			.andExpect(status().is(expectedStatusCode))
			.andExpect(content().string(""));
	}

	/**
	 * Publishes the provided domain events to the event bus.
	 *
	 * @param domainEvents the collection of domain events to be published to the event bus.
	 */
	protected void givenISendEventsToTheBus(DomainEvent... domainEvents) {
		eventBus.publish(Arrays.asList(domainEvents));
	}
}
