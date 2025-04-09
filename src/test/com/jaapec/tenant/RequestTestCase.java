package com.jaapec.tenant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class RequestTestCase {

	private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON;

	@Autowired
	private MockMvc mockMvc;

	protected void assertResponse(String endpoint, int expectedStatusCode, String expectedResponse) throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

		mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatusCode)).andExpect(response);
	}

	protected void assertResponse(String endpoint, int expectedStatusCode, String expectedResponse, HttpHeaders headers)
		throws Exception {
		ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

		mockMvc.perform(get(endpoint).headers(headers)).andExpect(status().is(expectedStatusCode)).andExpect(response);
	}

	protected void assertRequestWithBody(String method, String endpoint, String body, int expectedStatusCode)
		throws Exception {
		MockHttpServletRequestBuilder requestBuilder = request(HttpMethod.valueOf(method), endpoint)
			.content(body)
			.contentType(CONTENT_TYPE_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().is(expectedStatusCode)).andExpect(content().string(""));
	}

	protected void assertRequest(String method, String endpoint, int expectedStatusCode) throws Exception {
		MockHttpServletRequestBuilder requestBuilder = request(HttpMethod.valueOf(method), endpoint);

		mockMvc.perform(requestBuilder).andExpect(status().is(expectedStatusCode)).andExpect(content().string(""));
	}
}
