package ku.olga.core_api.provider

import ku.olga.core_api.repository.DirectionsRepository

interface DirectionsProvider {
    fun directionsRepository(): DirectionsRepository
}