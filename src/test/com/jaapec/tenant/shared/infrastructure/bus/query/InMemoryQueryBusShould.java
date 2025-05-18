package com.jaapec.tenant.shared.infrastructure.bus.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandlerExecutionError;
import com.jaapec.tenant.shared.domain.bus.query.QueryNotRegisteredError;
import com.jaapec.tenant.shared.domain.bus.query.Response;

class InMemoryQueryBusShould {

	private QueryHandlersInformation information;
	private ApplicationContext context;
	private InMemoryQueryBus queryBus;
	private TestQueryHandler handler;
	private TestResponse response;

	@BeforeEach
	void setUp() {
		information = mock(QueryHandlersInformation.class);
		context = mock(ApplicationContext.class);
		handler = spy(new TestQueryHandler());
		response = new TestResponse("test");
		queryBus = new InMemoryQueryBus(information, context);
	}

	@Test
	void ask_a_query_and_return_response() throws Exception {
		TestQuery query = new TestQuery();

		doReturn(TestQueryHandler.class).when(information).search(any());
		when(context.getBean(TestQueryHandler.class)).thenReturn(handler);
		when(handler.handle(query)).thenReturn(response);

		Response result = queryBus.ask(query);

		assertEquals(response, result);
		verify(handler).handle(query);
	}

	@Test
	void throw_an_exception_when_query_has_no_handler() throws QueryNotRegisteredError {
		TestQuery query = new TestQuery();

		doThrow(new QueryNotRegisteredError(TestQuery.class)).when(information).search(any());

		assertThrows(QueryHandlerExecutionError.class, () -> queryBus.ask(query));
	}

	@Test
	void throw_an_exception_when_handler_throws_an_exception() throws QueryNotRegisteredError {
		TestQuery query = new TestQuery();

		doReturn(TestQueryHandler.class).when(information).search(any());
		when(context.getBean(TestQueryHandler.class)).thenReturn(handler);
		when(handler.handle(query)).thenThrow(new RuntimeException("Error"));

		assertThrows(QueryHandlerExecutionError.class, () -> queryBus.ask(query));
	}

	static class TestQuery implements Query {}

	static class TestResponse implements Response {

		private final String value;

		public TestResponse(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	static class TestQueryHandler implements QueryHandler<TestQuery, TestResponse> {

		@Override
		public TestResponse handle(TestQuery query) {
			return new TestResponse("test");
		}
	}
}
