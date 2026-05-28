---
name: create-graphql-controller
description: Crear DataFetchers GraphQL (mutaciones y queries), schema y validación con el Validator del proyecto. Úsala para exponer cualquier caso de uso en la API.
---

# Create GraphQL Controller (DataFetcher)

## Estructura

Para cada bounded context:

```
<bc>/infrastructure/controller/
├── Request<X>.java                                       # input record
├── (Request<Y>.java, ...)
└── graphql/
    ├── <X>MutationsDataFetcher.java   o varios <X>CreateDataFetcher.java
    ├── <X>QueryDataFetcher.java
    └── schema/
        └── schema.graphqls
```

Hay dos estilos válidos en el repo:
- **Un DataFetcher por mutación** (`PlanCreateDataFetcher`, `PlanUpdateDataFetcher`, `PlanDeletionDataFetcher`...). Usado en `plans/`. Recomendado cuando cada mutación tiene su propio set de reglas de validación.
- **Un DataFetcher con todas las mutaciones del BC** (`TenantMutationsDataFetcher`). Usado en `tenant/`. Recomendado cuando comparten validación o son simples.

Para queries, generalmente uno solo: `<X>QueryDataFetcher`.

## Reglas

1. Hereda de `com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController` que aporta `dispatch(Command)` y `ask(Query)`.
2. Anotada con `@Controller` (Spring stereotype, **no** el `@Service` del proyecto).
3. Métodos con `@MutationMapping` o `@QueryMapping("nombreEnSchema")`.
4. Argumentos con `@Argument` (y `@Nullable` para opcionales como filtros/paginación).
5. **El UUID se genera aquí**: `String uuid = UUID.randomUUID().toString();`. El cliente nunca envía el id en `create`.
6. **Validación sintáctica con `Validator`**, que recibe un `Map<String, String>` de reglas tipo Laravel.
7. **Sin lógica de dominio en el DataFetcher**, solo orquestación y validación de input.

## Plantilla — Mutación con validación

```java
package com.jaapec.tenant.<bc>.infrastructure.controller.graphql.<X>;

import java.util.*;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.<bc>.application.<accion>.<Verbo><X>Command;
import com.jaapec.tenant.<bc>.domain.<X>Repository;
import com.jaapec.tenant.<bc>.domain.value_objects.<X>Id;
import com.jaapec.tenant.<bc>.infrastructure.controller.Request<X>;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLCustomException;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLExceptionList;
import com.jaapec.tenant.shared.infrastructure.validation.ValidationResponse;
import com.jaapec.tenant.shared.infrastructure.validation.Validator;
import com.jaapec.tenant.shared.infrastructure.validation.ValidatorNotExist;

@Controller
public final class <X>CreateDataFetcher extends GraphQLApiController {

	private final Validator validator;
	private final <X>Repository repository;
	private final Map<String, String> rules = Map.ofEntries(
		Map.entry("name", "required|not_empty|max:255|min:3"),
		Map.entry("description", "required|not_empty|max:255")
	);

	public <X>CreateDataFetcher(
		QueryBus queryBus,
		CommandBus commandBus,
		Validator validator,
		<X>Repository repository
	) {
		super(queryBus, commandBus);
		this.validator = validator;
		this.repository = repository;
	}

	@MutationMapping
	public boolean create<X>(@Argument Request<X> request) throws ValidatorNotExist {
		Jsonb jsonb = JsonbBuilder.create();
		String requestJson = jsonb.toJson(request);
		ValidationResponse validation = validator.validate(requestJson, rules, repository);

		List<GraphQLCustomException> errors = new ArrayList<>();
		if (Boolean.TRUE.equals(validation.hasErrors())) {
			validation.errors().forEach((key, values) ->
				values.forEach(error -> errors.add(new GraphQLCustomException(error, key)))
			);
			throw new GraphQLExceptionList(errors);
		}

		String uuid = UUID.randomUUID().toString();
		dispatch(
			new <Verbo><X>Command(
				new <X>Id(uuid).value(),
				request.name(),
				request.description()
			)
		);
		return true;
	}
}
```

## Plantilla — Query

```java
package com.jaapec.tenant.<bc>.infrastructure.controller.graphql;

import java.util.List;
import javax.annotation.Nullable;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.jaapec.tenant.<bc>.application.<X>Response;
import com.jaapec.tenant.<bc>.application.find.Find<X>Query;
import com.jaapec.tenant.<bc>.application.search.Search<X>Query;
import com.jaapec.tenant.shared.domain.bus.command.CommandBus;
import com.jaapec.tenant.shared.domain.bus.query.QueryBus;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.PaginatedResponse;
import com.jaapec.tenant.shared.domain.criteria.Pagination;
import com.jaapec.tenant.shared.infrastructure.controller.graphql.GraphQLApiController;

@Controller
public final class <X>QueryDataFetcher extends GraphQLApiController {

	public <X>QueryDataFetcher(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	@QueryMapping("find<X>")
	public <X>Response find<X>(@Argument String id) {
		return ask(new Find<X>Query(id));
	}

	@QueryMapping("search<X>s")
	public PaginatedResponse<<X>Response> search<X>s(
		@Argument List<Filter> filters,
		@Argument String orderBy,
		@Argument String orderType,
		@Argument @Nullable Integer limit,
		@Argument @Nullable Integer offset
	) {
		Pagination pagination = (limit == null || offset == null)
			? Pagination.defaults()
			: Pagination.fromValues(limit, offset);
		return ask(new Search<X>Query(filters != null ? filters : List.of(), orderBy, orderType, pagination));
	}
}
```

## Schema GraphQL

Ubicación: `<bc>/infrastructure/controller/graphql/schema/schema.graphqls`. El loader (`CustomDGSConfiguration`) los descubre automáticamente.

```graphql
input Request<X> {
    name: String!
    description: String!
}

type <X>Response {
    id: String!
    name: String!
    description: String!
    createdAt: String!
    updatedAt: String!
}

type Paginated<X>Response {
    data: [<X>Response!]!
    pagination: PaginationMetadata!
}

extend type Mutation {
    create<X>(request: Request<X>!): Boolean!
    update<X>(id: ID!, request: Request<X>!): Boolean!
    delete<X>(id: String!): Boolean!
}

extend type Query {
    find<X>(id: String!): <X>Response!
    search<X>s(
        filters: [FilterInput!]
        orderBy: String
        orderType: OrderType
        limit: Int
        offset: Int
    ): Paginated<X>Response!
}
```

> Tipos comunes (`FilterInput`, `OrderType`, `PaginationMetadata`, `Operator`) están en `shared/infrastructure/controller/graphql/schema/schema.graphqls`. **No los redefinas**.

## Reglas del `Validator`

Sintaxis tipo Laravel separada por `|`:

| Regla | Significado |
|---|---|
| `required` | El campo debe existir. |
| `not_empty` | No vacío (string, list). |
| `string` | Tipo string. |
| `integer` | Tipo entero. |
| `double` / `bigdecimal` | Tipos numéricos. |
| `uuid` | Formato UUID v4. |
| `email` | Formato email. |
| `enum:VAL1,VAL2` | Valor entre la lista. |
| `min:N` | Para strings: longitud >= N. Para números: valor >= N. |
| `max:N` | Inverso. |
| `regex:<pattern>` | Patrón. |
| `unique` | Único en BD (consulta el repository). |
| `date` / `datetime` | Formato fecha. |

Si añades una regla nueva, debes:
1. Crear un `Field<Tipo>Validator extends FieldValidator` en `shared/infrastructure/validation/validators/`.
2. Registrarlo en `Validator.validators` con su clave.
3. Añadir mensaje a `messages.properties`.

## RequestX records

Vive en `<bc>/infrastructure/controller/Request<X>.java`:

```java
package com.jaapec.tenant.<bc>.infrastructure.controller;

public record Request<X>(
	String name,
	String description
) {}
```

Las anotaciones GraphQL (`@Argument`) hacen el binding desde el input.

## Errores

- `GraphQLCustomException(message, fieldName)` para errores de validación individuales.
- `GraphQLExceptionList(List<GraphQLCustomException>)` para tirar todos los errores juntos. El `GraphQLExceptionResolver` los serializa con `extensions: {field}`.
- `DomainError` (lanzado desde el caso de uso) se serializa con `extensions: {code, reason, value}`.

## Checklist

- [ ] DataFetcher en `<bc>/infrastructure/controller/graphql/<X>/`.
- [ ] Hereda de `GraphQLApiController`, anotado `@Controller`.
- [ ] Inyecta `QueryBus`, `CommandBus`, y si valida, `Validator` y el `Repository`.
- [ ] Reglas de validación claras y completas.
- [ ] UUID se genera con `UUID.randomUUID().toString()` en mutaciones de creación.
- [ ] Schema añadido o extendido en `schema/schema.graphqls`.
- [ ] `Request<X>` record creado si hay input nuevo.
- [ ] Sin lógica de dominio.
- [ ] Tests de feature en `src/test/.../infrastructure/controller/graphql/<x>/<X>CreateDataFetcherShould.java` (ver skill `write-feature-test`).

## Referencias

- DataFetcher de creación con validación: `plans/infrastructure/controller/graphql/plan/PlanCreateDataFetcher.java`.
- DataFetcher unificado con varias mutaciones: `tenant/infrastructure/controller/graphql/TenantMutationsDataFetcher.java`.
- DataFetcher de queries: `tenant/infrastructure/controller/graphql/TenantQueryDataFetcher.java`.
- Schema compartido: `shared/infrastructure/controller/graphql/schema/schema.graphqls`.
- Schema de BC: `plans/infrastructure/controller/graphql/schema/schema.graphqls`.
