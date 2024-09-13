import graphql.schema.DataFetchingEnvironment

@Controller
class ResolverQL(
    private val openSearch: OpenSearchAsyncClient,
    private val configuration: MarketsConfiguration
) {

    private val dataProvider = ProviderQL(openSearch, configuration)

    @QueryMapping
    suspend fun search(
        @Argument page: Int,
        @Argument limit: Int,
        @Argument sortingType: Sorting,
        @Argument profileId: String,
        @Argument vehicleCategory: VehicleCategory,
        @Argument contextType: ContextType? = ContextType.B2C,
        @Argument language: String? = null,
        @Argument modelIdentifier: Set<String>? = null,
        @Argument bodyType: Set<String>? = null,
        @Argument brand: Set<String>? = null,
        @Argument modelYear: Range? = null,
        @Argument price: ValueRange? = null,
        @Argument monthlyRate: ValueRange? = null,
        @Argument campaigns: Set<String>? = null,
        @Argument fuelType: Set<String>? = null,
        @Argument driveType: Set<String>? = null,
        @Argument gearBox: Set<String>? = null,
        @Argument enginePowerKW: Range? = null,
        @Argument enginePowerHP: Range? = null,
        @Argument upholstery: Set<String>? = null,
        @Argument upholsteryName: Set<String>? = null,
        @Argument upholsteryPolish: Set<String>? = null,
        @Argument equipment: Set<String>? = null,
        @Argument packages: Set<String>? = null,
        @Argument lines: Set<String>? = null,
        @Argument color: Set<String>? = null,
        @Argument colorName: Set<String>? = null,
        @Argument colorPolish: Set<String>? = null,
        @Argument baumuster4: String? = null,
        @Argument variantId: String? = null,
        @Argument dealerId: Set<String>? = null,
        @Argument stockType: Set<String>? = null,
        @Argument payload: IntRange? = null,
        @Argument maximumWeight: IntRange? = null,
        @Argument dimensionsLength: IntRange? = null,
        @Argument dimensionsWidth: IntRange? = null,
        @Argument dimensionsHeight: IntRange? = null,
        @Argument seats: Set<String>? = null,
        @Argument estimatedArrivalDate: Range? = null,
        @Argument motorization: Set<String>? = null,
        @Argument typeClass: Set<String>? = null,
        @Argument faceLift: Int? = null,
        @Argument vinOrCommissionNumber: String? = null,
        @Argument modelDesignation: Set<String>? = null,
        @Argument torque: IntRange? = null,
        @Argument vehicleHeight: IntRange? = null,
        @Argument wheelBase: IntRange? = null,
        @Argument generation: Set<Generation>? = null,
        @Argument productionDate: Range? = null,
        @Argument buildType: Set<String>? = null,
        @Argument mileage: IntRange? = null,
        @Argument firstRegistrationDate: Range? = null,
        @Argument stockCategories: Set<String>? = null,
        environment: DataFetchingEnvironment,
    ): SearchResponseDto {
        return dataProvider.search(environment)
    }
}

/*
{
  search(page: 0, limit: 2, profileId: "AT-NEW_VEHICLES", vehicleCategory: PASSENGER_CARS, vehicleHeight: {min: 2, max: 4}) {
    results {
      identification {
        commissionNumber
      }
    }
  }
}
 */

