---
name: create-cqrs-command
description: Crear un slice completo de un caso de uso de escritura (Command + Handler + caso de uso de aplicación) que muta estado y publica eventos. Úsala para cualquier mutación nueva.
---

# Create CQRS Command Slice

## Estructura

Un slice de command vive completo en `application/<accion>/` y consta de **3 archivos**:

```
plans/application/create/
├── CreatePlanCommand.java          # record que implementa Command
├── CreatePlanCommandHandler.java   # @Service, traduce primitivos → VOs
└── PlanCreator.java                # @Service, lógica del caso de uso
```

Naming `<accion>`: snake_case (`create`, `update`, `delete`, `change_visibility`, `add_price`, `cancel_auto_renew`).

## Reglas

1. El **Command** es un `record` con primitivos, implementa `com.jaapec.tenant.shared.domain.bus.command.Command`.
2. El **Handler** es `@Service`, implementa `CommandHandler<TuCommand>`, **solo orquesta**: mapea primitivos a VOs y delega.
3. El **caso de uso** (ej. `PlanCreator`) es `@Service`, recibe el `Repository` + `EventBus`. Aquí va la lógica:
   - Si carga un agregado existente: `repository.find(id).orElseThrow(() -> new ResourceNotExist(...))`.
   - Llama el método de negocio del agregado (que devuelve nueva instancia).
   - Persiste con `repository.save/update/delete`.
   - Publica con `eventBus.publish(aggregate.pullDomainEvents())`.
4. **Validaciones de unicidad** se hacen aquí, **antes** de construir el agregado: `if (repository.uniqueField("name", name.value())) throw new ResourceAlreadyExists(...)`.
5. El handler devuelve `void`.
6. Una sola operación por handler.

## Plantillas

### 1) Command

```java
package com.jaapec.tenant.<bc>.application.<accion>;

import com.jaapec.tenant.shared.domain.bus.command.Command;

public record <Verbo><X>Command(
	String id,
	String name,
	int maxUsers
)
	implements Command {}
```

### 2) Command Handler

```java
package com.jaapec.tenant.<bc>.application.<accion>;

import com.jaapec.tenant.<bc>.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.command.CommandHandler;

@Service
public final class <Verbo><X>CommandHandler implements CommandHandler<<Verbo><X>Command> {

	private final <X><Verbo> useCase;

	public <Verbo><X>CommandHandler(<X><Verbo> useCase) {
		this.useCase = useCase;
	}

	@Override
	public void handle(<Verbo><X>Command command) {
		useCase.<verbo>(
			new <X>Id(command.id()),
			new <X>Name(command.name()),
			new <X>MaxUsers(command.maxUsers())
		);
	}
}
```

> Naming del field: si la clase del caso de uso se llama `PlanCreator`, el campo es `creator`. Si es `ChangeVisibility`, el campo es `visibility`. Mira los ejemplos del repo.

### 3) Caso de uso — creación

```java
package com.jaapec.tenant.<bc>.application.<accion>;

import com.jaapec.tenant.<bc>.domain.<X>;
import com.jaapec.tenant.<bc>.domain.<X>Repository;
import com.jaapec.tenant.<bc>.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.ResourceAlreadyExists;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.bus.event.EventBus;

@Service
public final class <X>Creator {

	private final <X>Repository repository;
	private final EventBus eventBus;

	public <X>Creator(<X>Repository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void create(<X>Id id, <X>Name name, <X>MaxUsers maxUsers) {
		if (repository.uniqueField("name", name.value())) {
			throw new ResourceAlreadyExists("<bc>", "name", name.value());
		}

		<X> instance = <X>.create(id, name, maxUsers);
		repository.save(instance);
		eventBus.publish(instance.pullDomainEvents());
	}
}
```

### 4) Caso de uso — mutación de existente

```java
@Service
public final class <X><Verbo> {  // p.ej. ChangeVisibility, PlanDeleter

	private final <X>Repository repository;
	private final EventBus eventBus;

	public <X><Verbo>(<X>Repository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void <verbo>(<X>Id id, /* otros VOs */) {
		<X> instance = repository.find(id)
			.orElseThrow(() -> new ResourceNotExist("<bc>", id.value()));

		<X> updated = instance.<verbo>(/* otros VOs */);
		repository.update(updated);
		eventBus.publish(updated.pullDomainEvents());
	}
}
```

### 5) Caso de uso — borrado

```java
@Service
public final class <X>Deleter {

	private final <X>Repository repository;
	private final EventBus eventBus;

	public <X>Deleter(<X>Repository repository, EventBus eventBus) {
		this.repository = repository;
		this.eventBus = eventBus;
	}

	public void delete(<X>Id id) {
		<X> instance = repository.find(id)
			.orElseThrow(() -> new ResourceNotExist("<bc>", id.value()));

		repository.delete(instance);
		eventBus.publish(Collections.singletonList(new <X>DeletedDomainEvent(instance.id().value())));
	}
}
```

## Test unitario obligatorio

Ubicación: `src/test/.../<bc>/application/<accion>/<Verbo><X>CommandHandlerShould.java`.

```java
package com.jaapec.tenant.<bc-test>.application.<accion>;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.<bc-test>.<X>ModuleUnitTestCase;
import com.jaapec.tenant.<bc-test>.domain.*;
import com.jaapec.tenant.<bc>.application.<accion>.*;
import com.jaapec.tenant.<bc>.domain.<X>;
import com.jaapec.tenant.<bc>.domain.events.<X>CreatedDomainEvent;

final class <Verbo><X>CommandHandlerShould extends <X>ModuleUnitTestCase {

	private <Verbo><X>CommandHandler handler;

	@Override
	@BeforeEach
	protected void setUp() {
		super.setUp();
		handler = new <Verbo><X>CommandHandler(new <X><Verbo>(repository, eventBus));
	}

	@Test
	void <verbo>_a_valid_<bc>() {
		<Verbo><X>Command command = <Verbo><X>CommandMother.random();
		<X> aggregate = <X>Mother.fromRequest(command);
		<X>CreatedDomainEvent event = <X>CreatedDomainEventMother.fromAggregate(aggregate);

		handler.handle(command);

		shouldHaveSaved(aggregate);
		shouldHavePublished(event);
	}
}
```

Y el Mother del Command:

```java
public final class <Verbo><X>CommandMother {
	public static <Verbo><X>Command create(<X>Id id, <X>Name name) {
		return new <Verbo><X>Command(id.value(), name.value());
	}

	public static <Verbo><X>Command random() {
		return create(<X>IdMother.random(), <X>NameMother.random());
	}
}
```

## Cómo se registra automáticamente

`CommandHandlersInformation` (en `shared/infrastructure/bus/command/`) escanea con Reflections todas las implementaciones de `CommandHandler` en `com.jaapec` y las indexa por su tipo de Command. **No tienes que registrar nada manualmente**: con que la clase tenga `@Service` (la del proyecto) y un constructor adecuado, el bus lo encuentra.

## Checklist

- [ ] Carpeta `application/<accion>/` con los 3 archivos.
- [ ] `<Verbo><X>Command` es record que implementa `Command`.
- [ ] `<Verbo><X>CommandHandler` es `@Service` y solo mapea primitivos → VOs.
- [ ] `<X><Verbo>` (caso de uso) es `@Service`, hace `find/save/update/delete + publish`.
- [ ] Errores de unicidad lanzan `ResourceAlreadyExists`. Errores de inexistencia lanzan `ResourceNotExist`.
- [ ] El agregado registra el evento; el caso de uso lo publica.
- [ ] Existe test `<Verbo><X>CommandHandlerShould` con `shouldHaveSaved` + `shouldHavePublished`.
- [ ] Existe `<Verbo><X>CommandMother`.

## Referencias

- Slice de creación: `plans/application/create/`.
- Slice de mutación cargando agregado: `plans/application/change_visibility/`.
- Slice de borrado: `plans/application/delete/`.
- Slice complejo con relación entre BCs: `tenant/application/add_subscription/`.
