package io.mb.onesearch.os4vs.core.graphql.data

import graphql.schema.DataFetchingEnvironment

class ProviderQL(
    private val openSearch: OpenSearchAsyncClient,
    private val configuration: MarketsConfiguration) {

    val mapper = MapperQL()
    val aux = AuxQL()

    suspend fun search(environment: DataFetchingEnvironment): SearchResponseDto {

        val parameters = mapper.mapper(environment.arguments, configuration)

        val requiredFields = mutableListOf<String>()

        if (parameters.contextType == ContextType.B2B) {
            requiredFields.add(B2B_PRICE)
        }

        val filters = mapOf(
            STATE to DOCUMENT_STATE_FILTER
        )

        val sort = aux.getSorting(parameters)

        val request = SearchRequest.Builder()
//            .index(aux.indexName(parameters))
            .index("os4vs-at-20240723175953")
            .query(parameters.toQuery(filters, requiredFields))
            .sort(sort)
            .from(parameters.page * parameters.pageLimit)
            .size(parameters.pageLimit)
            .build()

        return openSearch.search<VehicleDocument>(request)
            .toSearchResponseDto(2, parameters, removeEmptyResult = true)
    }

}
