import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import directives.TrimDirective
import directives.UppercaseDirective
import resolver.ResolverFetcherQL
import resolver.ResolverQL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class ConfigQL {

    @Bean
    fun graphQL(resolver: ResolverQL): GraphQL {
        val schemaFilePath = "model/schema.graphqls"
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

