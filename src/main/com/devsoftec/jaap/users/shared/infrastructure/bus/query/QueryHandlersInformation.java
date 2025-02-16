package com.devsoftec.jaap.users.shared.infrastructure.bus.query;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.domain.query.Query;
import com.devsoftec.jaap.users.shared.domain.query.QueryHandler;
import com.devsoftec.jaap.users.shared.domain.query.QueryNotRegisteredError;
import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Set;

@Service
public final class QueryHandlersInformation {
    HashMap<Class<? extends Query>, Class<? extends QueryHandler>> indexedQueryHandlers;

    public QueryHandlersInformation() {
        Reflections reflections = new Reflections("com.devsoftec");
        Set<Class<? extends QueryHandler>> classes     = reflections.getSubTypesOf(QueryHandler.class);

        indexedQueryHandlers = formatHandlers(classes);
    }

    private HashMap<Class<? extends Query>, Class<? extends QueryHandler>> formatHandlers(
            Set<Class<? extends QueryHandler>> queryHandlers
    ) {
        HashMap<Class<? extends Query>, Class<? extends QueryHandler>> handlers = new HashMap<>();

        for (Class<? extends QueryHandler> handler : queryHandlers) {
            ParameterizedType paramType  = (ParameterizedType) handler.getGenericInterfaces()[0];
            Class<? extends Query> queryClass = (Class<? extends Query>) paramType.getActualTypeArguments()[0];

            handlers.put(queryClass, handler);
        }

        return handlers;
    }

    public Class<? extends QueryHandler> search(Class<? extends Query> queryClass) throws QueryNotRegisteredError {
        Class<? extends QueryHandler> queryHandlerClass = indexedQueryHandlers.get(queryClass);

        if (null == queryHandlerClass) {
            throw new QueryNotRegisteredError(queryClass);
        }

        return queryHandlerClass;
    }
}