# GraphQL search - POC (Mercedes-Benz.io)

Search engine endpoint migration from REST to GraphQL (POC) - Integration, Creation, Performance & Optimization.

## Overview

The POC showcases how GraphQL can be used for the `os4vs` core search endpoint, providing a flexible and efficient alternative to REST.

## Implementation (missing whole project config)

### Schema

- **Queries**:
  ```graphql
  type Query {
      search(profileId: String!, vehicleCategory: VehicleCategory!,  language: String, modelIdentifier: [VehicleClass!]): [SearchResult]
  }           # required             # required          .....        # optional          # optional
    ```

    - Type 'search' contains all filters and also the fields that are filters as well (required and non required). With that, filtering logic is
  easier to implement.

      [schema](./search.graphqls)

### Data 

- **Provider**:
    - Responsible for retrieving the data from opensearch and filtering it based on the query parameters (mapped before by mapper).

      [provider](./data/ProviderQL.kt)

    ```kt
    return openSearch.search<VehicleDocument>(request)
            .toSearchResponseDto(2, parameters, removeEmptyResult = true)
    ```

- **Mapper**:
    - Responsible for mapping GraphQL query arguments to a SearchParameters object.
    - It validates and processes the input arguments, ensuring they conform to expected formats and values.

        [mapper](./data/MapperQL.kt)

- **Aux**:
    - Auxiliary class that contains functions to help data providing and mapping.

      [aux](./data/AuxQl.kt)

### Resolver

- **Resolver**:
    - The class contains one single custom resolver that relies on a data provider instance to fetch and filter the data (based on the 'search' type filters), returning the relevant results that match the given criteria (plays with a data environment instance as context).
    - We only use 1 resolver. We want to access all the data just one time and do the filtering needed instantly.

      [resolver](./resolver/ResolverQL.kt)

  ```kt
    suspend fun search(@Argument page: Int, @Argument limit: Int, @Argument sortingType: Sorting, @Argument profileId: String, @Argument vehicleCategory: VehicleCategory, @Argument contextType: ContextType? = ContextType.B2C, @Argument language: String? = null, @Argument modelIdentifier: Set<String>? = null, @Argument bodyType: Set<String>? = null, @Argument brand: Set<String>? = null, @Argument modelYear: Range? = null, @Argument price: ValueRange? = null, @Argument monthlyRate: ValueRange? = null, @Argument campaigns: Set<String>? = null, @Argument fuelType: Set<String>? = null, @Argument driveType: Set<String>? = null, @Argument gearBox: Set<String>? = null, @Argument enginePowerKW: Range? = null, @Argument enginePowerHP: Range? = null, @Argument upholstery: Set<String>? = null, @Argument upholsteryName: Set<String>? = null, @Argument upholsteryPolish: Set<String>? = null, @Argument equipment: Set<String>? = null, @Argument packages: Set<String>? = null, @Argument lines: Set<String>? = null, @Argument color: Set<String>? = null, @Argument colorName: Set<String>? = null, @Argument colorPolish: Set<String>? = null, @Argument baumuster4: String? = null, @Argument variantId: String? = null, @Argument dealerId: Set<String>? = null, @Argument stockType: Set<String>? = null, @Argument payload: IntRange? = null, @Argument maximumWeight: IntRange? = null, @Argument dimensionsLength: IntRange? = null, @Argument dimensionsWidth: IntRange? = null, @Argument dimensionsHeight: IntRange? = null, @Argument seats: Set<String>? = null, @Argument estimatedArrivalDate: Range? = null, @Argument motorization: Set<String>? = null, @Argument typeClass: Set<String>? = null, @Argument faceLift: Int? = null, @Argument vinOrCommissionNumber: String? = null, @Argument modelDesignation: Set<String>? = null, @Argument torque: IntRange? = null, @Argument vehicleHeight: IntRange? = null, @Argument wheelBase: IntRange? = null, @Argument generation: Set<Generation>? = null, @Argument productionDate: Range? = null, @Argument buildType: Set<String>? = null, @Argument mileage: IntRange? = null, @Argument firstRegistrationDate: Range? = null, @Argument stockCategories: Set<String>? = null, environment: DataFetchingEnvironment)
    : SearchResponseDto {
        return dataProvider.search(environment)
    }
    ```

## Considerations

**TO DO:**
- Revision of the params (like vin) and conditions missing;
- Search parameters validation;
- Grouping logic;
- OS return in provider (api version number & index are hard coded);
- Performance testing.

