---
name: create-aggregate-root
description: Crear un nuevo Aggregate Root o agregar comportamiento a uno existente respetando inmutabilidad, factories estáticas y registro de eventos. Úsala siempre que vayas a tocar una clase que extienda AggregateRoot.
---

# Create Aggregate Root

## Reglas no negociables

1. Extiende `com.jaapec.tenant.shared.domain.AggregateRoot`.
2. `final class` con todos los campos `private final`.
3. **Dos constructores**:
   - Público con todos los Value Objects.
   - Package-private sin args (`Foo()`) que pone todo a `null`. **Hibernate lo necesita**.
4. **Métodos de acceso estilo `name()`**, no `getName()`.
5. **Inmutable.** Toda operación de negocio devuelve **una nueva instancia** y registra el evento correspondiente con `record(...)`.
6. **Factory estática `create(...)`** que registra el evento de creación.
7. **`equals` y `hashCode` manuales** excluyendo campos volátiles (`createdAt`/`updatedAt`).
8. **No publica eventos.** Solo `record(event)`. El caso de uso de aplicación llama a `pullDomainEvents()`.

## Plantilla

```java
package com.jaapec.tenant.<bc>.domain;

import java.util.Objects;

import com.jaapec.tenant.<bc>.domain.events.<X>CreatedDomainEvent;
import com.jaapec.tenant.<bc>.domain.events.<X>UpdatedDomainEvent;
import com.jaapec.tenant.<bc>.domain.value_objects.*;
import com.jaapec.tenant.shared.domain.AggregateRoot;
import com.jaapec.tenant.shared.domain.DateUtils;

public final class <X> extends AggregateRoot {

	private final <X>Id id;
	private final <X>Name name;
	// ... otros VOs
	private final <X>CreatedAt createdAt;
	private final <X>UpdatedAt updatedAt;

	public <X>(
		<X>Id id,
		<X>Name name,
		// ... otros VOs en el mismo orden que arriba
		<X>CreatedAt createdAt,
		<X>UpdatedAt updatedAt
	) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	<X>() {
		this.id = null;
		this.name = null;
		this.createdAt = null;
		this.updatedAt = null;
	}

	public <X>Id id() { return id; }
	public <X>Name name() { return name; }
	public <X>CreatedAt createdAt() { return createdAt; }
	public <X>UpdatedAt updatedAt() { return updatedAt; }

	public static <X> create(<X>Id id, <X>Name name /* ... */) {
		String now = DateUtils.nowAsString();
		<X> instance = new <X>(
			id,
			name,
			// ...
			new <X>CreatedAt(now),
			new <X>UpdatedAt(now)
		);
		instance.record(
			new <X>CreatedDomainEvent(
				id.value(),
				name.value()
				// ... primitivos, no VOs
			)
		);
		return instance;
	}

	public <X> update(<X>Name newName /* ... */) {
		String now = DateUtils.nowAsString();
		<X> updated = new <X>(
			this.id,
			newName,
			this.createdAt,
			new <X>UpdatedAt(now)
		);
		updated.record(
			new <X>UpdatedDomainEvent(
				this.id.value(),
				newName.value(),
				updated.updatedAt().value()
			)
		);
		return updated;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		<X> that = (<X>) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
		return result;
	}
}
```

## Variantes según el caso

### Agregado con colección de entities

Mira `Plan` con `List<PlanPrice>`:

```java
public Plan addPrice(PlanPriceId id, BillingInterval interval, Amount amount, Currency currency) {
	String now = DateUtils.nowAsString();
	List<PlanPrice> newPrices = new ArrayList<>(
		Optional.ofNullable(this.prices).map(List::copyOf).orElseGet(List::of)
	);
	newPrices.add(new PlanPrice(id, interval, amount, currency, this,
		new PlanPriceCreatedAt(now), new PlanPriceUpdatedAt(now)));
	return new Plan(/* ...todos los campos pero con newPrices y new Plan UpdatedAt(now)... */);
}
```

Nota: la entity hija (`PlanPrice`) no es AggregateRoot, no registra eventos. Su mapping XML define la relación `<many-to-one>` al padre.

### Agregado que valida invariantes complejos

Mira `Tenant.subscribeToPlan`:

```java
public Tenant subscribeToPlan(...) {
	List<TenantPlanSubscription> currentSubs = Optional.ofNullable(this.subscriptions).orElse(List.of());

	if (currentSubs.stream().anyMatch(TenantPlanSubscription::isActive)) {
		throw new ActiveSubscriptionAlreadyExistsException();
	}
	if (currentSubs.stream().anyMatch(s -> s.paymentStatus().isPending())) {
		throw new PendingSubscriptionExistsException();
	}
	// ...
}
```

La validación de **invariantes de negocio** vive en el agregado. La validación **sintáctica** (no vacío, longitud) vive en `Validator` del DataFetcher.

## Checklist al crear/extender un Aggregate Root

- [ ] Clase `final` que extiende `AggregateRoot`.
- [ ] Campos `private final`.
- [ ] Constructor público con todos los VOs en el orden del XML mapping.
- [ ] Constructor package-private sin args con todo a `null`.
- [ ] Métodos de acceso `nombre()` (no `getNombre()`).
- [ ] Factory `create(...)` que setea timestamps con `DateUtils.nowAsString()` y registra el evento de creación.
- [ ] Cada operación de negocio devuelve una nueva instancia y registra evento.
- [ ] `equals`/`hashCode` excluyen `createdAt`/`updatedAt`.
- [ ] El XML mapping (`<X>.orm.xml`) refleja todos los campos.
- [ ] Existe Mother (`<X>Mother.java`) con `create(...)`, `random()`.
- [ ] Existen Mothers para cada VO usado.

## Errores comunes

- **Mutar `this`**: prohibido. Siempre `return new <X>(...)`.
- **Olvidar registrar el evento**: si añades una operación de negocio nueva, **siempre** crea su `<X><Verbo>DomainEvent` y llama a `record(...)`.
- **Constructor sin args ausente**: Hibernate explota silenciosamente al hidratar. Síntoma: `InstantiationException`.
- **Pasar VOs al evento en lugar de primitivos**: los eventos viajan serializados; siempre pasa `vo.value()`.
- **Publicar el evento desde el agregado**: NO. El agregado solo registra. La publicación la hace `<Aggregate>Creator/Updater/Deleter`.

## Referencias

- Plantilla simple: `src/main/com/jaapec/tenant/plans/domain/Plan.java`.
- Plantilla rica con subentities y validaciones: `src/main/com/jaapec/tenant/tenant/domain/Tenant.java`.
