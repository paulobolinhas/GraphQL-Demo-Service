class ModelQL {

    data class Query(
        val document: List<Document>?
    )

    data class Document(
        val profileId: String,
        val vehicleCategory: VehicleCategory,
        val identification: Identification?
    )

    data class Identification(
        val commissionNumber: String?,
        val vin: String?,
        val licensePlate: String?
    )

    enum class VehicleCategory {
        PASSENGER_CARS,
        VANS
    }

}
