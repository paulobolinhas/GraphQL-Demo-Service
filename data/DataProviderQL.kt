package io.mb.onesearch.os4vs.core.graphql.data

import io.mb.onesearch.os4vs.core.graphql.model.ModelQL.*
import io.mb.onesearch.os4vs.core.graphql.resolver.ResolverQL

class DataProviderQL {

    private val allDocuments = listOf(
        Document(
            profileId = "doc1_profile",
            vehicleCategory = VehicleCategory.PASSENGER_CARS,
            identification = Identification(
                commissionNumber = "123456",
                vin = "1HGCM82633A004352",
                licensePlate = "XYZ-1234"
            )
        ),
        Document(
            profileId = "doc1_profile",
            vehicleCategory = VehicleCategory.PASSENGER_CARS,
            identification = Identification(
                commissionNumber = "123457",
                vin = "2HGCM82633A004353",
                licensePlate = "ABC-5678"
            )
        ),
        Document(
            profileId = "doc3_profile",
            vehicleCategory = VehicleCategory.PASSENGER_CARS,
            identification = Identification(
                commissionNumber = "345678",
                vin = "3HGCM82633A004354",
                licensePlate = "DEF-9012"
            )
        )
    )

    fun getFilteredResults(profileId: String, vehicleCategory: VehicleCategory,
                           commissionNumber: String? = null, vin: String? = null): List<Document> {
        return allDocuments.filter { document ->
            val requestedNonRequiredFilters = listOfNotNull(
                commissionNumber?.let { document.identification?.commissionNumber == it },
                vin?.let { document.identification?.vin == it }
            )

            document.profileId == profileId &&
            document.vehicleCategory == vehicleCategory &&
            requestedNonRequiredFilters.all { it }
        }
    }
}
