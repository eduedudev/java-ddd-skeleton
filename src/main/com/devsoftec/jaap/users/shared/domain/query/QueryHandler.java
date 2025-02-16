package com.devsoftec.jaap.users.shared.domain.query;

public interface QueryHandler<Q extends Query, R extends Response> {
    R handle(Q query);
}
