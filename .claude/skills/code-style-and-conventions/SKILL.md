---
name: code-style-and-conventions
description: Estilo de código, naming, dependencias permitidas y reglas de Spotless para el tenant-service. Consúltala antes de escribir cualquier archivo Java o si el formato del PR no pasa.
---

# Code Style & Conventions

## Java version y formato

- **Java 25** (toolchain en `build.gradle`).
- **Spotless con prettier-java 2.2.0**. Configuración:
  - `useTabs: true`
  - `printWidth: 120`
  - Import order: `\\#`, `java`, ``, `com.jaapec`, ``
  - Remove unused imports automáticamente.
  - End with newline.
  - Format annotations.
- Antes de subir cambios: `./gradlew spotlessApply`.

## Naming

| Elemento | Convención | Ejemplo |
|---|---|---|
| Aggregate Root | Sustantivo singular | `Plan`, `Tenant` |
| Value Object | Sufijo descriptivo del concepto | `PlanId`, `Amount`, `BillingInterval`, `TenantDomain` |
| Domain Event | `<Aggregate><Verbo>DomainEvent` | `PlanCreatedDomainEvent`, `TenantDomainChangedEvent` |
| Command | `<Verbo><Aggregate>Command` | `CreatePlanCommand`, `ChangeVisibilityPlanCommand` |
| Query | `<Verbo><Aggregate>Query` | `FindPlanQuery`, `SearchPlanQuery` |
| Command Handler | `<Verbo><Aggregate>CommandHandler` | `CreatePlanCommandHandler` |
| Query Handler | `<Verbo><Aggregate>QueryHandler` | `FindPlanQueryHandler` |
| Caso de uso (servicio aplicación) | `<Aggregate><Verbo>` | `PlanCreator`, `PlanDeleter`, `TenantDomainChanger` |
| Repository (puerto) | `<Aggregate>Repository` | `PlanRepository` |
| Repository (adaptador) | `MariaDB<Aggregate>Repository` | `MariaDBPlanRepository` |
| GraphQL DataFetcher | `<Aggregate><Accion>DataFetcher` o `<Aggregate>MutationsDataFetcher` | `PlanCreateDataFetcher`, `TenantQueryDataFetcher` |
| Domain Error | `<Razon>Exception` | `InvalidDomainException`, `MinValueException` |
| Test class | `<ClaseBajoTest>Should` | `CreatePlanCommandHandlerShould` |
| Object Mother | `<Clase>Mother` | `PlanMother`, `CreatePlanCommandMother` |
| Carpetas de application | snake_case | `change_visibility/`, `add_subscription/` |

## Estructura de una clase

```java
package com.jaapec.tenant.<bounded_context>.<layer>.<sub>;

// 1. imports java
import java.util.List;

// 2. imports de terceros (sin com.jaapec)
import org.hibernate.SessionFactory;

// 3. imports del proyecto
import com.jaapec.tenant.shared.domain.Service;

@Service
public final class PlanCreator {
    // campos final
    private final PlanRepository repository;
    private final EventBus eventBus;

    // constructor
    public PlanCreator(PlanRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    // métodos públicos
    public void create(...) { ... }

    // métodos privados
}
```

## Reglas duras

1. **`final` en clases concretas siempre.** Solo deja la clase abierta si es abstract o si está pensada para ser extendida (ej. `AggregateRoot`, `DomainEvent`, `Identifier`, `StringValueObject`).
2. **Campos `private final`** salvo cuando Hibernate exige `non-final` (raro: solo en algunos Value Objects que reusan setter access — en este proyecto **no** los hay).
3. **Sin Lombok.** Equals, hashCode, toString manuales.
4. **`record` para DTOs:** Commands, Queries, Responses, `Filter`, `Order`, `Pagination`, `Request<X>` GraphQL inputs.
5. **Sin getters `getX()`.** Usa `x()` (estilo records, también en clases normales).
6. **`@Service` siempre es `com.jaapec.tenant.shared.domain.Service`**, nunca `org.springframework.stereotype.Service`. La diferencia importa: `Starter.java` filtra component scan por esa anotación.
7. **`@Transactional` sólo en repositorios MariaDB y en `MariaDBEventBus`.** Casos de uso no lo llevan (la transacción la abre el repo).
8. **Evita `var`** en firmas públicas y campos. En cuerpos de método locales puede usarse cuando el tipo es obvio.
9. **Streams**: prefiere `.toList()` (Java 16+) sobre `.collect(Collectors.toList())`.
10. **Optional**: nunca `.get()` sin chequeo. Prefiere `.orElseThrow(() -> new ResourceNotExist(...))` o `.map(...).orElseThrow(...)`.

## Dependencias permitidas

Las que están en `build.gradle`. **No introduzcas otras** sin discutirlo:

| Categoría | Librería |
|---|---|
| Web | Spring Boot Webmvc Starter |
| GraphQL | Netflix DGS Spring GraphQL Starter |
| ORM | Hibernate Core 7.3.4, Spring ORM, Spring Data JPA |
| DB | mariadb-java-client (runtime), DataSourceBuilder |
| Mensajería | spring-boot-starter-amqp |
| JSON | jakarta.json + jakarta.json.bind + yasson |
| Logging | log4j-core, log4j-api |
| Reflexión | reflections 0.10.2 |
| Util | guava, dotenv-java |
| DNS | dnsjava |
| Actuator | spring-boot-starter-actuator |
| Tests | JUnit 5, Mockito, DataFaker, dgs-spring-graphql-starter-test |

**Prohibido sin acuerdo previo**: Lombok, MapStruct, AssertJ-DB, Testcontainers (aún no está integrado), librerías de validación bean (Hibernate Validator), librerías de JSON alternativas (Jackson directo, Gson).

## Logging

- Usa `Log4j2Logger` (`shared.infrastructure.spring.log`) o el bean `Logger` (`shared.domain.Logger`) inyectado.
- No hagas `System.out.println` (excepto en el `Config` legacy donde ya está; no lo extiendas).

## i18n

- Toda mensaje de error de usuario va en `src/main/resources/messages.properties` con clave `error.<area>.<detalle>`.
- En código se referencia con `new Message("error.foo.bar", new Object[]{argA, argB})`.

## Comentarios

- Javadoc en abstracciones públicas reutilizables (`HibernateRepository`, `ApplicationTestCase`, `DomainError`).
- En código de caso de uso normal, evita javadoc obvio. Un nombre claro vale más.
- TODO/FIXME deben tener una referencia a issue/Jira o ser eliminados antes de mergear.

## Ejemplo: VO con formato canónico

```java
package com.jaapec.tenant.plans.domain.value_objects;

import com.jaapec.tenant.plans.domain.NonNegativeNumberException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class Amount extends IntValueObject {

	private static final int MIN_PRICE = 0;

	public Amount(Integer value) {
		super(ensureValidPrice(value));
	}

	public Amount() {
		super(null);
	}

	private static Integer ensureValidPrice(Integer value) {
		if (value < MIN_PRICE) throw new NonNegativeNumberException("Amount", value.toString());
		return value;
	}
}
```

Nota: tabs indentation, `final class`, dos constructores (público + package-private vacío para Hibernate), método estático de validación, excepción de dominio en caso de fallo.
