package ku.olga.route_builder.presentation.dagger.module

import android.content.Context
import com.google.maps.GeoApiContext
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.route_builder.R
import ku.olga.route_builder.data.repository.GoogleDirectionsRepository
import ku.olga.route_builder.data.repository.OSRMDirectionsRepository
import ku.olga.route_builder.domain.repository.DirectionsRepository
import ku.olga.route_builder.presentation.dagger.annotation.ActivityScope
import ku.olga.route_builder.presentation.dagger.annotation.ApplicationContext

@Module(includes = [DirectionsModule.BindsModule::class])
class DirectionsModule {
    @Provides
    @Reusable
    fun providesGeoApiContext(@ApplicationContext context: Context) = GeoApiContext.Builder()
        .apiKey(context.getString(R.string.google_api_key))
        .build()

    @Module
    interface BindsModule {
        @ActivityScope
        @Binds
        fun bindsDirectionsRepository(repository: OSRMDirectionsRepository): DirectionsRepository
    }
}