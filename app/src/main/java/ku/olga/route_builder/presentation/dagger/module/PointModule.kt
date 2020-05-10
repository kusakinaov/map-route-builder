package ku.olga.route_builder.presentation.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ku.olga.route_builder.data.repository.PointsDbCacheRepository
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
class PointModule {
    @Singleton
    @Provides
    fun providesPointsCacheRepository(appDatabase: AppDatabase): PointsCacheRepository =
        PointsDbCacheRepository(appDatabase)

    @Provides
    fun providesAppDatabase(@Named("application_context") context: Context): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "route-builder-database")
            .fallbackToDestructiveMigration()
            .build()
}