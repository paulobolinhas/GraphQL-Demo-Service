package com.example.graphql

import com.expediagroup.graphql.server.spring.GraphQLSchemaConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfig {

    @Bean
    fun graphQLSchemaConfiguration(): GraphQLSchemaConfiguration {
        return GraphQLSchemaConfiguration()
    }
}
