---
name: create-domain-event
description: Crear un Domain Event y, si aplica, su jerarquía abstracta común. Úsala cuando agregues una operación nueva en un agregado o un nuevo agregado.
---

# Create Domain Event

## Reglas

1. Hereda de `com.jaapec.tenant.shared.domain.bus.event.DomainEvent`.
2. Vive en `<bc>/domain/events/`.
3. **Si un agregado tiene varios eventos con muchos campos comunes**, crea un `abstract <X>DomainEvent` y especializa: `<X>CreatedDomainEvent`, `<X>UpdatedDomainEvent`. Si el evento es muy distinto (`PlanDeletedDomainEvent`), hereda directo de `DomainEvent`.
4. Implementa **3 métodos** obligatorios:
   - `String eventName()` → formato `"<bc>.<verbo>"` en minúsculas (`plan.created`, `tenant.subscribed_to_plan`).
   - `Map<String, Serializable> toPrimitives()` → serialización para el bus.
   - `DomainEvent fromPrimitives(aggregateId, body, eventId, occurredOn)` → deserialización desde el bus.
5. **Tres constructores** (más uno package-private vacío si el evento se va a deserializar desde RabbitMQ):
   - Sin `aggregateId` (para registros antes de tener id, raro).
   - Con `aggregateId` (caso normal cuando se crea desde el agregado).
   - Con `aggregateId + eventId + occurredOn` (para reconstruir desde primitivos).
6. **Los argumentos del evento son siempre `String`** (o tipos `Serializable`), nunca Value Objects. En el agregado conviertes con `vo.value()`.
7. **Sin lógica de negocio** en el evento. Es solo un DTO inmutable con metadatos.

## Plantilla — abstract base + especializaciones

### Abstract base

```java
package com.jaapec.tenant.<bc>.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public abstract class <X>DomainEvent extends DomainEvent {

	protected final String name;
	protected final String description;
	// ...campos comunes

	protected <X>DomainEvent(
		String name,
		String description
	) {
		this.name = name;
		this.description = description;
	}

	protected <X>DomainEvent(
		String aggregateId,
		String name,
		String description
	) {
		super(aggregateId);
		this.name = name;
		this.description = description;
	}

	protected <X>DomainEvent(
		String aggregateId,
		String eventId,
		String occurredOn,
		String name,
		String description
	) {
		super(aggregateId, eventId, occurredOn);
		this.name = name;
		this.description = description;
	}

	protected <X>DomainEvent() {
		super(null);
		this.name = null;
		this.description = null;
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		Map<String, Serializable> primitives = new HashMap<>();
		primitives.put("name", name);
		primitives.put("description", description);
		return primitives;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		<X>DomainEvent that = (<X>DomainEvent) o;
		return Objects.equals(name, that.name) && Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(description);
		return result;
	}
}
```

### Especialización

```java
package com.jaapec.tenant.<bc>.domain.events;

import java.io.Serializable;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class <X>CreatedDomainEvent extends <X>DomainEvent {

	public <X>CreatedDomainEvent(String name, String description) {
		super(null, name, description);
	}

	public <X>CreatedDomainEvent(String aggregateId, String name, String description) {
		super(aggregateId, name, description);
	}

	public <X>CreatedDomainEvent(
		String aggregateId,
		String eventId,
		String occurredOn,
		String name,
		String description
	) {
		super(aggregateId, eventId, occurredOn, name, description);
	}

	<X>CreatedDomainEvent() {
		super(null, null, null, null, null);
	}

	@Override
	public String eventName() {
		return "<bc>.created";
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new <X>CreatedDomainEvent(
			aggregateId,
			eventId,
			occurredOn,
			(String) body.get("name"),
			(String) body.get("description")
		);
	}
}
```

## Plantilla — Evento minimalista (sin payload)

```java
package com.jaapec.tenant.<bc>.domain.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jaapec.tenant.shared.domain.bus.event.DomainEvent;

public final class <X>DeletedDomainEvent extends DomainEvent {

	<X>DeletedDomainEvent() {
		super(null);
	}

	public <X>DeletedDomainEvent(String aggregateId) {
		super(aggregateId);
	}

	public <X>DeletedDomainEvent(String aggregateId, String eventId, String occurredOn) {
		super(aggregateId, eventId, occurredOn);
	}

	@Override
	public String eventName() {
		return "<bc>.deleted";
	}

	@Override
	public Map<String, Serializable> toPrimitives() {
		return new HashMap<>();
	}

	@Override
	public DomainEvent fromPrimitives(
		String aggregateId,
		Map<String, Serializable> body,
		String eventId,
		String occurredOn
	) {
		return new <X>DeletedDomainEvent(aggregateId, eventId, occurredOn);
	}
}
```

## Mother

```java
package com.jaapec.tenant.<bc>.domain;

import com.jaapec.tenant.<bc>.domain.<X>;
import com.jaapec.tenant.<bc>.domain.events.<X>CreatedDomainEvent;
import com.jaapec.tenant.<bc>.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.DateUtils;

public final class <X>CreatedDomainEventMother {

	public static <X>CreatedDomainEvent create(<X>Id id, <X>Name name) {
		return new <X>CreatedDomainEvent(id.value(), name.value());
	}

	public static <X>CreatedDomainEvent fromAggregate(<X> aggregate) {
		return create(aggregate.id(), aggregate.name());
	}

	public static <X>CreatedDomainEvent random() {
		return create(<X>IdMother.random(), <X>NameMother.random());
	}
}
```

## Disparo desde el agregado

```java
public static <X> create(<X>Id id, <X>Name name /* ... */) {
	String now = DateUtils.nowAsString();
	<X> instance = new <X>(id, name /* ... */,
		new <X>CreatedAt(now), new <X>UpdatedAt(now));

	instance.record(new <X>CreatedDomainEvent(
		id.value(),
		name.value()
	));

	return instance;
}
```

## Publicación desde el caso de uso

```java
@Service
public final class <X>Creator {
	private final <X>Repository repository;
	private final EventBus eventBus;

	public void create(<X>Id id, <X>Name name) {
		<X> instance = <X>.create(id, name);
		repository.save(instance);
		eventBus.publish(instance.pullDomainEvents()); // <-- aquí
	}
}
```

## Configuración SonarQube

Los eventos están **excluidos de cobertura** en `build.gradle`:

```
property "sonar.coverage.exclusions", """
    **/*DomainEvent.java
"""
```

No te preocupes por test específico de `XxxDomainEventShould`. La cobertura efectiva la da el test del handler que verifica `shouldHavePublished(event)`.

## Checklist

- [ ] El evento vive en `<bc>/domain/events/`.
- [ ] Hereda de `DomainEvent` o de la base abstracta `<X>DomainEvent`.
- [ ] Implementa `eventName()`, `toPrimitives()`, `fromPrimitives()`.
- [ ] Tiene 4 constructores (3 normales + uno vacío para deserialización).
- [ ] El agregado lo registra con `record(...)`.
- [ ] El caso de uso publica con `eventBus.publish(aggregate.pullDomainEvents())`.
- [ ] Existe Mother correspondiente.
- [ ] Test del handler verifica `shouldHavePublished(event)`.

## Referencias

- Base abstracta + especializaciones: `plans/domain/events/PlanDomainEvent.java` y `plans/domain/events/PlanCreatedDomainEvent.java`.
- Evento sin payload: `plans/domain/events/PlanDeletedDomainEvent.java`.
- Contrato base: `shared/domain/bus/event/DomainEvent.java`.
- Serialización JSON: `shared/infrastructure/bus/event/DomainEventJsonSerializer.java`.
