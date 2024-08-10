package com.example.graphql

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class HelloWorldQueryResolver : Query {
    fun hello(): String = "Hello, World!"
}
