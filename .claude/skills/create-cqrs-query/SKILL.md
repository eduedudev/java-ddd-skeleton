---
name: create-cqrs-query
description: Crear un slice completo de un caso de uso de lectura (Query + Handler + Finder/Searcher + Response) sin efectos secundarios. Úsala para cualquier consulta nueva.
---

# Create CQRS Query Slice

## Estructura

Una query simple (`find`, `findByX`):

```
plans/application/find/
├── FindPlanQuery.java          # record que implementa Query
├── FindPlanQueryHandler.java   # @Service
└── PlanFinder.java             # @Service, devuelve PlanResponse
```

Una query con filtrado/paginación (`search`):

```
plans/application/search/
├── SearchPlanQuery.java        # record con List<Filter>, orderBy, orderType, Pagination
├── SearchPlanQueryHandler.java # @Service
└── PlanSearcher.java           # @Service, devuelve PaginatedResponse<PlanResponse>
```

El Response es compartido entre todas las queries del bounded context y vive en `application/<X>Response.java`.

## Reglas

1. La **Query** es un `record` que implementa `com.jaapec.tenant.shared.domain.bus.query.Query`.
2. El **Response** es un `record` que implementa `com.jaapec.tenant.shared.domain.bus.query.Response`.
3. El **Handler** implementa `QueryHandler<TuQuery, TuResponse>` y devuelve el response.
4. **Sin efectos secundarios**: ni `save`, ni `update`, ni `eventBus.publish`.
5. **Sin lanzar `DomainError`** salvo `ResourceNotExist` cuando un find no encuentra el agregado.
6. El response tiene un método estático `fromAggregate(<X> aggregate)` que mapea de dominio a DTO.

## Plantillas

### 1) Find (un agregado por id)

```java
// application/find/FindPlanQuery.java
package com.jaapec.tenant.<bc>.application.find;

import com.jaapec.tenant.shared.domain.bus.query.Query;

public record Find<X>Query(String id) implements Query {}
```

```java
// application/find/Find<X>QueryHandler.java
package com.jaapec.tenant.<bc>.application.find;

import com.jaapec.tenant.<bc>.application.<X>Response;
import com.jaapec.tenant.<bc>.domain.value_objects.<X>Id;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;

@Service
public final class Find<X>QueryHandler implements QueryHandler<Find<X>Query, <X>Response> {

	private final <X>Finder finder;

	public Find<X>QueryHandler(<X>Finder finder) {
		this.finder = finder;
	}

	@Override
	public <X>Response handle(Find<X>Query query) {
		return finder.find(new <X>Id(query.id()));
	}
}
```

```java
// application/find/<X>Finder.java
package com.jaapec.tenant.<bc>.application.find;

import com.jaapec.tenant.<bc>.application.<X>Response;
import com.jaapec.tenant.<bc>.domain.<X>Repository;
import com.jaapec.tenant.<bc>.domain.value_objects.<X>Id;
import com.jaapec.tenant.shared.domain.ResourceNotExist;
import com.jaapec.tenant.shared.domain.Service;

@Service
public final class <X>Finder {

	private final <X>Repository repository;

	public <X>Finder(<X>Repository repository) {
		this.repository = repository;
	}

	public <X>Response find(<X>Id id) {
		return repository
			.find(id)
			.map(<X>Response::fromAggregate)
			.orElseThrow(() -> new ResourceNotExist("<bc>", id.value()));
	}
}
```

### 2) Search (con criteria + paginación)

```java
// application/search/Search<X>Query.java
package com.jaapec.tenant.<bc>.application.search;

import java.util.List;

import com.jaapec.tenant.shared.domain.bus.query.Query;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.Pagination;

public record Search<X>Query(
	List<Filter> filters,
	String orderBy,
	String orderType,
	Pagination pagination
)
	implements Query {}
```

```java
// application/search/Search<X>QueryHandler.java
package com.jaapec.tenant.<bc>.application.search;

import com.jaapec.tenant.<bc>.application.<X>Response;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.query.QueryHandler;
import com.jaapec.tenant.shared.domain.criteria.Filters;
import com.jaapec.tenant.shared.domain.criteria.Order;
import com.jaapec.tenant.shared.domain.criteria.PaginatedResponse;

@Service
public final class Search<X>QueryHandler
	implements QueryHandler<Search<X>Query, PaginatedResponse<<X>Response>> {

	private final <X>Searcher searcher;

	public Search<X>QueryHandler(<X>Searcher searcher) {
		this.searcher = searcher;
	}

	@Override
	public PaginatedResponse<<X>Response> handle(Search<X>Query query) {
		Filters filters = Filters.fromValues(query.filters());
		Order order = Order.from(query.orderBy(), query.orderType());
		return searcher.search(filters, order, query.pagination());
	}
}
```

```java
// application/search/<X>Searcher.java
package com.jaapec.tenant.<bc>.application.search;

import java.util.List;

import com.jaapec.tenant.<bc>.application.<X>Response;
import com.jaapec.tenant.<bc>.domain.<X>Repository;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.criteria.*;

@Service
public final class <X>Searcher {

	private final <X>Repository repository;

	public <X>Searcher(<X>Repository repository) {
		this.repository = repository;
	}

	public PaginatedResponse<<X>Response> search(Filters filters, Order order, Pagination pagination) {
		Criteria criteria = new Criteria(filters, order, pagination);
		long total = repository.count(criteria);
		List<<X>Response> data = repository.matching(criteria).stream()
			.map(<X>Response::fromAggregate)
			.toList();
		PaginationMetadata metadata = PaginationMetadata.calculateMetadata(pagination, total);
		return new PaginatedResponse<>(data, metadata);
	}
}
```

### 3) Response

Vive en `application/<X>Response.java` (no en una subcarpeta):

```java
package com.jaapec.tenant.<bc>.application;

import com.jaapec.tenant.<bc>.domain.<X>;
import com.jaapec.tenant.shared.domain.bus.query.Response;

public record <X>Response(
	String id,
	String name,
	String createdAt,
	String updatedAt
)
	implements Response {

	public static <X>Response fromAggregate(<X> aggregate) {
		return new <X>Response(
			aggregate.id().value(),
			aggregate.name().value(),
			aggregate.createdAt().value(),
			aggregate.updatedAt().value()
		);
	}
}
```

Si el agregado tiene colecciones, mapea cada item con su propio `<Y>Response.fromAggregate(...)`. Mira `PlanResponse` con `prices: List<PriceResponse>`.

## Mother del Response

```java
package com.jaapec.tenant.<bc-test>.application;

import com.jaapec.tenant.<bc-test>.domain.<X>Mother;
import com.jaapec.tenant.<bc>.application.<X>Response;
import com.jaapec.tenant.<bc>.domain.<X>;

public final class <X>ResponseMother {

	public static <X>Response create(<X> aggregate) {
		return new <X>Response(
			aggregate.id().value(),
			aggregate.name().value(),
			aggregate.createdAt().value(),
			aggregate.updatedAt().value()
		);
	}

	public static <X>Response random() {
		return create(<X>Mother.random());
	}
}
```

## Test unitario

```java
final class Find<X>QueryHandlerShould extends <X>ModuleUnitTestCase {

	private Find<X>QueryHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		handler = new Find<X>QueryHandler(new <X>Finder(repository));
	}

	@Test
	void return_<bc>_when_it_exists() {
		<X> aggregate = <X>Mother.random();
		Find<X>Query query = new Find<X>Query(aggregate.id().value());
		<X>Response expected = <X>ResponseMother.create(aggregate);

		shouldSearch(aggregate);

		assertEquals(expected, handler.handle(query));
	}

	@Test
	void return_not_found_when_<bc>_does_not_exist() {
		Find<X>Query query = new Find<X>Query(<X>IdMother.random().value());
		shouldSearch();
		assertThrows(ResourceNotExist.class, () -> handler.handle(query));
	}
}
```

`shouldSearch(...)` y `shouldSearch()` los aporta `<X>ModuleUnitTestCase`.

## Detalles de criteria / paginación

- `Pagination.defaults()` = `(20, 0)`.
- `Pagination.fromValues(limit, offset)` con: `limit` capado a 100, `offset = max(offset - 1, 0)` (paginación 1-indexed por fuera).
- `Order.from(field, direction)`: si `field` es null, default `"createdAt"`. Si `direction` es null o inválido, `Direction.NONE` y no se aplica orden.
- Operadores soportados: `EQUAL`, `NOT_EQUAL`, `GT`, `LT`, `CONTAINS`, `NOT_CONTAINS`.

## Checklist

- [ ] Carpeta `application/<accion>/` con Query + Handler + Finder/Searcher.
- [ ] Response en `application/<X>Response.java` con `fromAggregate`.
- [ ] Sin efectos secundarios.
- [ ] `Find` usa `repository.find(id).orElseThrow(() -> new ResourceNotExist(...))`.
- [ ] `Search` usa `Criteria(filters, order, pagination)` y devuelve `PaginatedResponse`.
- [ ] Existen Mothers del Response.
- [ ] Tests con `shouldSearch(aggregate)` y `shouldSearch()` para los dos caminos.

## Referencias

- Find: `plans/application/find/`.
- Search: `plans/application/search/` y test `SearchPlanQueryHandlerShould`.
- Response con sub-collections: `plans/application/PlanResponse.java`.
