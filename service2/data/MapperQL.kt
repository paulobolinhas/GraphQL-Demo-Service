class MapperQL {

    val aux = AuxQL()
    var market: String = ""
    lateinit var productType: ProductType

    fun mapper(arguments: Map<String, Any>, configuration: MarketsConfiguration): SearchParameters {
        val profileId = (arguments["profileId"] as? String)?.uppercase()?.trim() ?: ""
        market = profileId.split("-").firstOrNull()?.trim() ?: ""

        val id = profileId.split("-")
            .takeIf { it.size == 2 }
            ?.lastOrNull()
            ?.takeIf { ALLOWED_PROFILE_VALUES.containsKey(it) }
            ?: ""

        require(market.matches("[A-Z]{2}".toRegex()) && id.isNotEmpty()) { "Invalid profileId!" }

        val marketProfile = configuration.markets[market]
            .takeIf { id.isNotEmpty() }

        require(marketProfile != null) { "No configuration was found for selected profileId!" }

        productType = ProductType.fromProfileId(profileId)
        require(aux.isProductSupported(productType, marketProfile!!)) {
            val searchType = when (productType) {
                ProductType.NEW_VEHICLES -> "new"
                ProductType.USED_VEHICLES -> "used"
            }
            "The search for $searchType vehicles is not supported by this market!"
        }

        val vehicleCategory = (arguments["vehicleCategory"] as? String)?.uppercase()?.trim()

        val selectedCategory = ALLOWED_PROFILE_VALUES[id]?.let { it(marketProfile) }
            ?.firstOrNull { it.id == vehicleCategory }

        require(selectedCategory != null) { "Invalid vehicle category!" }

        val selectedContextType = aux.selectedContextType(arguments)

        val selectedLanguage = (arguments["languages"] as? String)
            ?: marketProfile.languages.takeIf { it.size == 1 }?.firstOrNull()
                ?.lowercase()

        require(selectedLanguage != null) { "Language parameter is required!" }
        require(selectedLanguage.matches("[a-z]{2}".toRegex()) && id.isNotEmpty()) { "Invalid language!" }
        require(marketProfile.languages.contains(selectedLanguage)) { "Unknown language for market!" }

        val selectedSorting = (arguments["sortingType"] as? String)?.lowercase()?.replace("_", "-")

        require(validSorting(selectedSorting, marketProfile, id, selectedCategory.id)) { "Sorting type is not valid for market!" }

        return SearchParameters(
            market = market,
            id = id,
            marketProfile = marketProfile,
            profileId = profileId,
            productType = productType,
            vehicleCategory = vehicleCategory.toString(),
            selectedCategory = selectedCategory,
            contextType = selectedContextType,
            language = selectedLanguage,
            modelIdentifier = arguments["modelIdentifier"] as? Set<String> ?: emptySet(),
            modelYear = arguments["modelYear"] as? Range,
            bodyType = arguments["bodyType"] as? Set<String> ?: emptySet(),
            brand = arguments["brand"] as? Set<String> ?: emptySet(),
            price = arguments["price"] as? Range,
            monthlyRate = arguments["monthlyRate"] as? Range,
            campaigns = arguments["campaigns"] as? Set<String> ?: emptySet(),
            fuelType = arguments["fuelType"] as? Set<String> ?: emptySet(),
            driveType = arguments["driveType"] as? Set<String> ?: emptySet(),
            gearbox = arguments["gearbox"] as? Set<String> ?: emptySet(),
            enginePowerKW = arguments["enginePowerKW"] as? Range,
            enginePowerHp = arguments["enginePowerHp"] as? Range,
            upholstery = arguments["upholstery"] as? Set<String> ?: emptySet(),
            upholsteryPolish = arguments["upholsteryPolish"] as? Set<String> ?: emptySet(),
            upholsteryName = arguments["upholsteryName"] as? Set<String> ?: emptySet(),
            equipment = arguments["equipment"] as? Set<String> ?: emptySet(),
            packages = arguments["packages"] as? Set<String> ?: emptySet(),
            lines = arguments["lines"] as? Set<String> ?: emptySet(),
            color = arguments["color"] as? Set<String> ?: emptySet(),
            colorName = arguments["colorName"] as? Set<String> ?: emptySet(),
            colorPolish = arguments["colorPolish"] as? Set<String> ?: emptySet(),
            baumuster4 = arguments["baumuster4"] as? String,
            pageLimit = arguments["pageLimit"] as? Int ?: 10,
            page = arguments["page"] as? Int ?: arguments["pageOffset"] as? Int ?: 0,
            sortingType = selectedSorting?.let {
                val (fieldName, order) = it.split("-")
                Sort(fieldName, order)
            },
            variantId = arguments["variantId"] as? String,
            dealerId = arguments["dealerId"] as? Set<String> ?: emptySet(),
            stockType = arguments["stockType"] as? Set<StockItemState> ?: emptySet(),
            payload = arguments["payload"] as? Range,
            length = arguments["length"] as? Range,
            width = arguments["width"] as? Range,
            height = arguments["height"] as? Range,
            volume = arguments["volume"] as? Range,
            seats = arguments["seats"] as? Set<String> ?: emptySet(),
            maximumWeight = arguments["maximumWeight"] as? Range,
            estimatedArrivalDate = arguments["estimatedArrivalDate"] as? Range,
            productionDate = arguments["productionDate"] as? Range,
            buildType = arguments["buildType"] as? Set<String> ?: emptySet(),
            isOnBehalfOf = arguments["isOnBehalfOf"] as? Boolean ?: false,
            motorization = arguments["motorization"] as? Set<String> ?: emptySet(),
            typeClass = arguments["typeClass"] as? Set<String> ?: emptySet(),
            faceLift = arguments["faceLift"] as? String,
            vinOrCommissionNumber = arguments["vinOrCommissionNumber"] as? String,
            modelDesignation = arguments["modelDesignation"] as? Set<String> ?: emptySet(),
            torque = arguments["torque"] as? Range,
            wheelBase = arguments["wheelBase"] as? Range,
            vehicleHeight = arguments["vehicleHeight"] as? Range,
            generation = arguments["generation"] as? Set<String> ?: emptySet(),
            mileage = arguments["mileage"] as? Range,
            firstRegistrationDate = arguments["firstRegistrationDate"] as? Range,
            stockCategory = arguments["stockCategory"] as? Set<String> ?: emptySet()
        )
    }
}
