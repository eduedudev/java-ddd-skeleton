package com.jaapec.tenant.shared.infrastructure.controller.graphql;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.jaapec.tenant.shared.infrastructure.Config;

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

		List<Resource> schemaResources = searchMappingFiles("/infrastructure/controller/graphql/schema/", ".graphqls");

		if (schemaResources.isEmpty()) {
			String defaultSchema = "type Query { _empty: String } type Mutation { _empty: String }";
			registry.merge(schemaParser.parse(defaultSchema));
			return registry;
		}

		for (Resource resource : schemaResources) {
			try (
				InputStream inputStream = resource.getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)
			) {
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
