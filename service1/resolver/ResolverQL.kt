import com.expediagroup.graphql.server.operations.Query
import data.DataProviderQL
import model.ModelQL
import model.ModelQL.*
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
