package ku.olga.route_builder.presentation.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.route_builder.data.repository.PointsDbCacheRepository
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import ku.olga.route_builder.presentation.dagger.annotation.ActivityScope
import ku.olga.route_builder.presentation.dagger.annotation.ApplicationContext

@Module(includes = [PointModule.BindsModule::class])
class PointModule {
    @Provides
    @Reusable
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "route-builder-database")
            .fallbackToDestructiveMigration()
            .build()

    @Module
    interface BindsModule {
        @ActivityScope
        @Binds
        fun providesPointsCacheRepository(repository: PointsDbCacheRepository): PointsCacheRepository
    }
}