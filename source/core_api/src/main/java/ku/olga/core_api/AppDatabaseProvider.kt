package ku.olga.core_api

import ku.olga.core_api.database.AppDatabaseContract
import ku.olga.core_api.database.UserPointDao

interface AppDatabaseProvider {
    fun appDatabaseContract(): AppDatabaseContract
    fun userPointDao(): UserPointDao
}