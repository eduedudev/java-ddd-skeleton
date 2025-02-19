package com.devsoftec.jaap.users.shared.infrastructure.controller.graphql;

import com.devsoftec.jaap.users.shared.infrastructure.Config;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CustomDGSConfiguration extends Config {

    @Bean
    public RuntimeWiring runtimeWiring() {
        return RuntimeWiring.newRuntimeWiring().build();
    }

    @Bean
    public TypeDefinitionRegistry typeDefinitionRegistry(ApplicationContext applicationContext) throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry registry = new TypeDefinitionRegistry();

        List<Resource> schemaResources =searchMappingFiles("/infrastructure/controller/graphql/", ".graphqls");

        if (schemaResources.isEmpty()) {
            String defaultSchema = "type Query { _empty: String }";
            registry.merge(schemaParser.parse(defaultSchema));
            return registry;
        }

        for (Resource resource : schemaResources) {
            try (InputStream inputStream = resource.getInputStream();
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                registry.merge(schemaParser.parse(reader));
            }
        }

        return registry;
    }

    @Bean
    public GraphQLSchema graphQLSchema(TypeDefinitionRegistry typeRegistry, RuntimeWiring runtimeWiring) {
        return new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
    }
}


