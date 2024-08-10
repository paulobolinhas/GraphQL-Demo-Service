package io.mb.onesearch.os4vs.core.graphql.directives

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import org.slf4j.LoggerFactory

class UppercaseDirective : SchemaDirectiveWiring {
    override fun onField(env: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
        val originalFetcher = env.fieldDataFetcher
        val uppercaseFetcher = DataFetcher { dataEnv: DataFetchingEnvironment ->
            val result = originalFetcher.get(dataEnv) as String
            result.uppercase()
        }
        env.codeRegistry.dataFetcher(env.fieldsContainer as GraphQLObjectType?, env.fieldDefinition, uppercaseFetcher)
        return env.element
    }
}

class TrimDirective : SchemaDirectiveWiring {
    override fun onField(env: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
        val originalFetcher = env.fieldDataFetcher
        val trimFetcher = DataFetcher { dataEnv: DataFetchingEnvironment ->
            val result = originalFetcher.get(dataEnv) as? String ?: ""
            result.trim()
        }
        env.codeRegistry.dataFetcher(env.fieldsContainer as GraphQLObjectType?, env.fieldDefinition, trimFetcher)
        return env.element
    }
}








