package ku.olga.core_api.provider

import ku.olga.core_api.repository.POIRepository

interface POIRepositoryProvider {
    fun poiRepository(): POIRepository
}