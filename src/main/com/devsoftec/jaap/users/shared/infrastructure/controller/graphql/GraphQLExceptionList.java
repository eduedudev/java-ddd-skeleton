package com.devsoftec.jaap.users.shared.infrastructure.controller.graphql;

import java.util.List;

public class GraphQLExceptionList extends RuntimeException {
    private final List<GraphQLCustomException> errors;


    public GraphQLExceptionList(List<GraphQLCustomException> errors) {
        this.errors = errors;
    }

    public GraphQLExceptionList(){
        this.errors = null;
    }

    public List<GraphQLCustomException> getErrors() {
        return errors;
    }
}
