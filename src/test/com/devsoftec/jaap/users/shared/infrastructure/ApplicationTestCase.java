package com.devsoftec.jaap.users.shared.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Map;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.devsoftec.jaap.users.shared.domain.bus.event.DomainEvent;
import com.devsoftec.jaap.users.shared.domain.bus.event.EventBus;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApplicationTestCase {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EventBus eventBus;

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	protected void assertResponse(String endpoint, Integer expectedStatusCode, String expectedResponse) throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

		mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatusCode)).andExpect(response);
	}

	protected void assertResponse(
		@Language("GraphQL") String mutation,
		@Language("JSONPath") String jsonPath,
		Map<String, Object> variables
	) throws Exception {
		Boolean result = dgsQueryExecutor.executeAndExtractJsonPath(mutation, jsonPath, variables);
		assertTrue(result);
	}

	protected void assertResponseWithBody(
		@Language("GraphQL") String mutation,
		@Language("JSONPath") String jsonPath,
		Map<String, Object> variables,
		String expectedResponse
	) throws Exception {
		QueryException exception = assertThrows(
			QueryException.class,
			() -> {
				dgsQueryExecutor.executeAndExtractJsonPath(mutation, "$", variables);
			}
		);
		assertEquals(expectedResponse, exception.getMessage());
	}

	protected void assertResponse(
		String endpoint,
		Integer expectedStatusCode,
		String expectedResponse,
		HttpHeaders headers
	) throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

		mockMvc.perform(get(endpoint).headers(headers)).andExpect(status().is(expectedStatusCode)).andExpect(response);
	}

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

	protected void assertRequestWithBody(String method, String endpoint, String body, Integer expectedStatusCode)
		throws Exception {
		mockMvc
			.perform(request(HttpMethod.valueOf(method), endpoint).content(body).contentType(APPLICATION_JSON))
			.andExpect(status().is(expectedStatusCode))
			.andExpect(content().string(""));
	}

	protected void assertRequest(String method, String endpoint, Integer expectedStatusCode) throws Exception {
		mockMvc
			.perform(request(HttpMethod.valueOf(method), endpoint))
			.andExpect(status().is(expectedStatusCode))
			.andExpect(content().string(""));
	}

	protected void givenISendEventsToTheBus(DomainEvent... domainEvents) {
		eventBus.publish(Arrays.asList(domainEvents));
	}
}
