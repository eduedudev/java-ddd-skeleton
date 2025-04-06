package com.jaapec.tenant.shared.infrastructure.persistence.hibernate;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.SessionFactory;

import com.jaapec.tenant.shared.domain.ValueObjects.Identifier;
import com.jaapec.tenant.shared.domain.criteria.*;

public abstract class HibernateRepository<T> {

	protected final SessionFactory sessionFactory;
	protected final Class<T> aggregateClass;
	protected final HibernateCriteriaConverter<T> criteriaConverter;

	/**
	 * Constructor for the HibernateRepository class.
	 *
	 * @param sessionFactory The Hibernate SessionFactory
	 * @param aggregateClass The class of the aggregate
	 */
	public HibernateRepository(SessionFactory sessionFactory, Class<T> aggregateClass) {
		this.sessionFactory = sessionFactory;
		this.aggregateClass = aggregateClass;
		this.criteriaConverter = new HibernateCriteriaConverter<>(sessionFactory.getCriteriaBuilder());
	}

	/**
	 * Persists an instance of the aggregate class.
	 *
	 * @param entity The instance to persist
	 */
	protected void persist(T entity) {
		sessionFactory.getCurrentSession().persist(entity);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	protected void merge(T entity) {
		sessionFactory.getCurrentSession().merge(entity);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	/**
	 * Returns an instance of the aggregate class with the given id.
	 *
	 * @param id The id of the instance to retrieve
	 * @return An instance of the aggregate class with the given id, or null if no instance with the
	 *     given id exists
	 */
	protected Optional<T> byId(Identifier id) {
		return Optional.ofNullable(sessionFactory.getCurrentSession().byId(aggregateClass).load(id));
	}

	/**
	 * Returns a list of instances of the aggregate class that match the given criteria.
	 *
	 * @param criteria The criteria to match
	 * @return A list of instances of the aggregate class that match the given criteria
	 */
	protected List<T> byCriteria(Criteria criteria) {
		CriteriaQuery<T> hibernateCriteria = criteriaConverter.convert(criteria, aggregateClass);

		return sessionFactory
			.getCurrentSession()
			.createQuery(hibernateCriteria)
			.setFirstResult(criteria.pagination().offset())
			.setMaxResults(criteria.pagination().limit())
			.getResultList();
	}

	/**
	 * Counts the number of entities of the aggregate class that match the given criteria.
	 *
	 * @param criteria The criteria object containing filters, order, and pagination details to apply to the count query.
	 * @return The total count of entities matching the given criteria.
	 */
	protected long countByCriteria(Criteria criteria) {
		CriteriaQuery<T> hibernateCriteria = criteriaConverter.convert(criteria, aggregateClass);
		return sessionFactory.getCurrentSession().createQuery(hibernateCriteria).getResultStream().count();
	}

	/**
	 * Returns all instances of the aggregate class.
	 *
	 * @return A list of instances of the aggregate class
	 */
	protected List<T> all() {
		CriteriaQuery<T> criteria = sessionFactory.getCriteriaBuilder().createQuery(aggregateClass);

		criteria.from(aggregateClass);

		return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
	}

	/**
	 * Checks if a field value is unique in the database.
	 *
	 * @param fieldName The name of the field to check
	 * @param value The value of the field to check
	 * @return true if the field value is unique, false otherwise
	 */
	protected boolean isFieldValueUnique(String fieldName, String value) {
		Filter filter = new Filter(new FilterField(fieldName), FilterOperator.EQUAL, new FilterValue(value));
		Filters filters = new Filters(List.of(filter));
		Pagination pagination = Pagination.defaults();
		Criteria criteria = new Criteria(filters, Order.defaultOrder(), pagination);
		long count = countByCriteria(criteria);
		return count != 0;
	}
}
