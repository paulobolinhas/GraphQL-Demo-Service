package io.mb.onesearch.os4vs.core.graphql.config

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import io.mb.onesearch.os4vs.core.graphql.directives.TrimDirective
import io.mb.onesearch.os4vs.core.graphql.directives.UppercaseDirective
import io.mb.onesearch.os4vs.core.graphql.resolver.ResolverFetcherQL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import io.mb.onesearch.os4vs.core.graphql.resolver.ResolverQL

@Configuration
class ConfigQL {

    @Bean
    fun graphQL(resolver: ResolverQL): GraphQL {
        val schemaFilePath = "graphql/schema.graphqls"
        val schemaFile = ClassPathResource(schemaFilePath).inputStream

        val schemaParser = SchemaParser()
        val typeRegistry = schemaParser.parse(schemaFile)

        val resolverFetcherFactory = ResolverFetcherQL(resolver)
        val fetchers = resolverFetcherFactory.registerFetchers()

        val runtimeWiring = newRuntimeWiring()
            .type("Query") { builder ->
                fetchers.forEach { (field, fetcher) ->
                    builder.dataFetcher(field, fetcher)
                }
                builder
            }
            .directive("Uppercase", UppercaseDirective())
            .directive("Trim", TrimDirective())
            .build()

        val schemaGenerator = SchemaGenerator()
        val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)

        return GraphQL.newGraphQL(graphQLSchema).build()
    }
}

