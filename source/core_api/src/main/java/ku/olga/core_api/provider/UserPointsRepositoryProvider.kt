package ku.olga.core_api.provider

import ku.olga.core_api.database.AppDatabaseContract
import ku.olga.core_api.repository.PointsCacheRepository

interface UserPointsRepositoryProvider {
    fun userPointsRepository(): PointsCacheRepository
    fun appDatabaseContract(): AppDatabaseContract
}