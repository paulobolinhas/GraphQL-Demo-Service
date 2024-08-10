# GraphQL Demo Service

** **

## Demo running service

Running GraphQL Service (inside package 'demo')

### Implementation

Spring Initializr + GraphQL default setup

### Considerations

None

** **

## Service test

Migration from REST to GraphQL test (other packages)

### Implementation

#### Schema

- **Queries**:
  ```graphql
  type Query {
      document(profileId: String!, vehicleCategory: VehicleCategory!, commissionNumber: String, vin: String): [Document]
  }           # required             # required                         # optional          # optional
    ```

    - Type 'document' contains all filters and also the fields that are filters as well (required and non required). With that, filtering logic is
  easier to implement.

      [schema](./model/schema.graphqls)


- **Directives**:
    - `@Uppercase`: Transforms string fields to uppercase.
    - `@Trim`: Trims whitespace from string fields.

    - Directive logic (annotations) in order to acquire validation.

        [definitions](./directives/DirectivesDefsQL.kt)

        [directives](./directives/DirectivesDefsQL.kt)


- **Model**:
    - Represents, in kotlin, the GraphQL schema.

      [model](./model/ModelQL.kt)

#### Data Provider

- **Data Provider**:
    - Responsible for retrieving the data from the data source and filtering it based on the query parameters (passed on the resolver).
    - The class contains a method that filters data from the source by separating required and optional parameters, returning documents that match all specified criteria.

      [provider](./data/DataProviderQL.kt)

#### Resolver

- **Resolver**:
    - The class contains one single custom resolver that relies on a data provider instance to fetch and filter the data (based on the 'document' type filters),
    encapsulating the logic for querying and returning the relevant documents that match the given criteria.
    - Only use 1 resolver. Want to access all the data just one time and do the filtering needed instantly.

      [resolver](./resolver/ResolverQL.kt)

  ```kt
    fun document(
        profileId: String,
        vehicleCategory: VehicleCategory,
        commissionNumber: String?,
        vin: String?
    ): List<Document> {

        return dataProvider.getFilteredResults(profileId, vehicleCategory, commissionNumber, vin).toMutableList()
    }
    ```


- **Fetcher**:
    - Responsible for registering data fetchers that map the queries to their corresponding resolver methods. It acts as a bridge between the GraphQL schema and the underlying resolver logic.
    - Since it's done all in one custom resolver, only one fetcher is used.

      [fetcher](./resolver/ResolverFetcherQL.kt)

#### Configuration

- **Config**:
    - GraphQL configuration file.
    - Parses the schema, registering his custom directives such as @Uppercase and @Trim, and configures the data fetchers
      for the Query type using the ResolverFetcherQL. The class then generates and returns a GraphQL object, making the schema executable and ready for handling GraphQL queries in the application.

      [config](./config/ConfigQL.kt)

#### Considerations

- Filtering in levels (in the case where filters with the same name exist). - In that case, dependencies between resolvers are needed.


