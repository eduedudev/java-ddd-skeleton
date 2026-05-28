package com.jaapec.tenant.shared.infrastructure.persistence.hibernate;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import jakarta.persistence.criteria.*;

import com.jaapec.tenant.shared.domain.criteria.Criteria;
import com.jaapec.tenant.shared.domain.criteria.Filter;
import com.jaapec.tenant.shared.domain.criteria.FilterOperator;

public final class HibernateCriteriaConverter<T> {

	private final CriteriaBuilder builder;
	private final Map<FilterOperator, BiFunction<Filter, Root<T>, Predicate>> predicateTransformers = Map.of(
		FilterOperator.EQUAL,
		this::equalsPredicateTransformer,
		FilterOperator.NOT_EQUAL,
		this::notEqualsPredicateTransformer,
		FilterOperator.GT,
		this::greaterThanPredicateTransformer,
		FilterOperator.LT,
		this::lowerThanPredicateTransformer,
		FilterOperator.CONTAINS,
		this::containsPredicateTransformer,
		FilterOperator.NOT_CONTAINS,
		this::notContainsPredicateTransformer
	);

	public HibernateCriteriaConverter(CriteriaBuilder builder) {
		this.builder = builder;
	}

	public CriteriaQuery<T> convert(Criteria criteria, Class<T> aggregateClass) {
		CriteriaQuery<T> hibernateCriteria = builder.createQuery(aggregateClass);
		Root<T> root = hibernateCriteria.from(aggregateClass);

		hibernateCriteria.where(formatPredicates(criteria.filters().filters(), root));

		if (criteria.order().hasOrder()) {
			Path<Object> orderBy = root.get(criteria.order().field());
			Order order = criteria.order().direction().isAsc() ? builder.asc(orderBy) : builder.desc(orderBy);

			hibernateCriteria.orderBy(order);
		}

		return hibernateCriteria;
	}

	public CriteriaQuery<Long> convertCount(Criteria criteria, Class<T> aggregateClass) {
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<T> root = countQuery.from(aggregateClass);

		countQuery.select(builder.count(root));
		countQuery.where(formatPredicates(criteria.filters().filters(), root));

		return countQuery;
	}

	private Predicate[] formatPredicates(List<Filter> filters, Root<T> root) {
		List<Predicate> predicates = filters.stream().map(filter -> formatPredicate(filter, root)).toList();

		Predicate[] predicatesArray = new Predicate[predicates.size()];
		predicatesArray = predicates.toArray(predicatesArray);

		return predicatesArray;
	}

	private Predicate formatPredicate(Filter filter, Root<T> root) {
		BiFunction<Filter, Root<T>, Predicate> transformer = predicateTransformers.get(filter.operator());
		return transformer.apply(filter, root);
	}

	private Predicate equalsPredicateTransformer(Filter filter, Root<T> root) {
		return builder.equal(root.get(filter.field().value()).get("value"), filter.value().value());
	}

	private Predicate notEqualsPredicateTransformer(Filter filter, Root<T> root) {
		return builder.notEqual(root.get(filter.field().value()).get("value"), filter.value().value());
	}

	private Predicate greaterThanPredicateTransformer(Filter filter, Root<T> root) {
		return builder.greaterThan(root.get(filter.field().value()).get("value"), filter.value().value());
	}

	private Predicate lowerThanPredicateTransformer(Filter filter, Root<T> root) {
		return builder.lessThan(root.get(filter.field().value()).get("value"), filter.value().value());
	}

	private Predicate containsPredicateTransformer(Filter filter, Root<T> root) {
		return builder.like(root.get(filter.field().value()).get("value"), String.format("%%%s%%", filter.value().value()));
	}

	private Predicate notContainsPredicateTransformer(Filter filter, Root<T> root) {
		return builder.notLike(
			root.get(filter.field().value()).get("value"),
			String.format("%%%s%%", filter.value().value())
		);
	}
}
