package io.mb.onesearch.os4vs.core.graphql.data

import org.opensearch.client.opensearch._types.FieldValue
import org.opensearch.client.opensearch._types.SortOptions
import org.opensearch.client.opensearch._types.SortOrder
import org.opensearch.client.opensearch._types.query_dsl.Query

class AuxQL {

    fun getSorting(parameters: SearchParameters) =
        parameters.sortingType?.let { sortingType ->
            check(SORTING_FIELDS.contains(sortingType.fieldName)) { "Unknown sorting field" }

            getSortOrder(sortingType).toSortOptions(getSortField(sortingType, parameters), parameters)
        }

    private fun getSortOrder(sortingType: Sort) = when (sortingType.order) {
        "asc" -> SortOrder.Asc
        "desc" -> SortOrder.Desc

        else -> error("Unknown sorting order!")
    }

    private fun SortOrder.toSortOptions(sortField: String, parameters: SearchParameters) = SortOptions.Builder()
        .field { field ->
            field.field(SORT_MAPPING[sortField])
            if (sortField in SORT_MAPPING_NESTED) {
                field.nested { nested ->
                    nested.path(SORT_MAPPING_NESTED[sortField]?.get(NESTED_SORTING_PATH))
                    nested.filter(Query.Builder().terms { terms ->
                        terms.field(SORT_MAPPING_NESTED[sortField]?.get(NESTED_SORTING_FILTER_FIELD))
                        terms.terms { termsField ->
                            termsField.value(setOf(parameters.language).map { item -> FieldValue.of(item) })
                        }
                    }.build())
                }
            }
            field.order(this)
        }
        .build()

    private fun getSortField(sortingType: Sort, parameters: SearchParameters) = when (sortingType.fieldName) {
        PRICE -> contextField(parameters.contextType, B2B_PRICE, B2C_PRICE)
        RATE -> contextField(parameters.contextType, B2B_RATE, B2C_RATE)
        else -> sortingType.fieldName
    }

    private fun contextField(contextType: ContextType, b2bField: String, b2cField: String): String =
        when (contextType) {
            ContextType.B2B -> b2bField
            ContextType.B2C -> b2cField
        }

    fun castToStringSetMap(value: Any?): Map<String, Set<String>> {
        return (value as? Map<*, *>)?.mapNotNull {
            val key = it.key as? String
            val valueSet = (it.value as? List<*>)?.mapNotNull { item -> item as? String }?.toSet()
            if (key != null && valueSet != null) key to valueSet else null
        }?.toMap() ?: emptyMap()
    }

    fun selectedContextType(arguments: Map<String, Any>): ContextType {
        val rawValue = arguments["contextType"] as? List<*> ?: return ContextType.B2C
        require(rawValue.size <= 1) { "Invalid context type! Too many definitions. Only one allowed!" }

        return when (rawValue[0]?.toString()?.trim()?.uppercase()) {
            ContextType.B2B.name -> ContextType.B2B
            ContextType.B2C.name -> ContextType.B2C
            else -> throw IllegalArgumentException("Invalid context type!")
        }
    }

    fun isProductSupported(productType: ProductType, marketProfile: MarketProfile): Boolean =
        when (productType) {
            ProductType.NEW_VEHICLES -> marketProfile.newVehicles.isNotEmpty()
            ProductType.USED_VEHICLES -> marketProfile.usedVehicles.isNotEmpty()
        }

    fun indexName(parameters: SearchParameters) = indexName(parameters.market, parameters.productType)
    private fun indexName(market: String, productType: ProductType) = "os4vs-${productType.type}-${market.lowercase()}"


}
