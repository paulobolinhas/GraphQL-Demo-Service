package io.mb.onesearch.os4vs.core.graphql.resolver

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import io.mb.onesearch.os4vs.core.graphql.model.ModelQL

class ResolverFetcherQL(private val resolver: ResolverQL) {

    fun registerFetchers(): Map<String, DataFetcher<*>> {
        return mapOf(
            "document" to createFetcher { dataEnv ->
                val profileId = dataEnv.getArgument<String>("profileId")
                val vehicleCategory = ModelQL.VehicleCategory.valueOf(dataEnv.getArgument<String>("vehicleCategory"))
                val commissionNumber = dataEnv.getArgument<String?>("commissionNumber") // optional argument
                val vin = dataEnv.getArgument<String?>("vin") // optional argument

                resolver.document(
                    profileId = profileId,
                    vehicleCategory = vehicleCategory,
                    commissionNumber = commissionNumber,
                    vin = vin
                )
            }
        )
    }

    private fun <T> createFetcher(method: (DataFetchingEnvironment) -> T): DataFetcher<T> {
        return DataFetcher { dataEnv -> method(dataEnv) }
    }
}
