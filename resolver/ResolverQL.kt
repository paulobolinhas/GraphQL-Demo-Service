package io.mb.onesearch.os4vs.core.graphql.resolver

import com.expediagroup.graphql.server.operations.Query
import io.mb.onesearch.os4vs.core.graphql.data.DataProviderQL
import io.mb.onesearch.os4vs.core.graphql.model.ModelQL
import io.mb.onesearch.os4vs.core.graphql.model.ModelQL.*
import org.springframework.stereotype.Component

@Component
class ResolverQL : Query {

    private val dataProvider = DataProviderQL()

    fun document(
        profileId: String,
        vehicleCategory: VehicleCategory,
        commissionNumber: String?,
        vin: String?
    ): List<Document> {

        return dataProvider.getFilteredResults(profileId, vehicleCategory, commissionNumber, vin).toMutableList()
    }

}
