package ku.olga.core_impl.dagger

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.core_impl.repository.PointsDbCacheRepository
import ku.olga.core_api.repository.PointsCacheRepository
import ku.olga.core_api.annotation.ApplicationContext
import ku.olga.core_api.database.AppDatabaseContract
import ku.olga.core_impl.room.AppDatabase

@Module(includes = [UserPointCacheModule.BindsModule::class])
class UserPointCacheModule {
    @Provides
    @Reusable
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabaseContract =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "route-builder-database")
            .fallbackToDestructiveMigration()
            .build()

    @Module
    interface BindsModule {
        @Binds
        fun providesPointsCacheRepository(repository: PointsDbCacheRepository): PointsCacheRepository
    }
}