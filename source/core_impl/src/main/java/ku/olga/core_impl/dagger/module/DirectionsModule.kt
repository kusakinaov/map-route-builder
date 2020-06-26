package ku.olga.core_impl.dagger.module

import android.content.Context
import com.google.maps.GeoApiContext
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.core_api.annotation.ApplicationContext
import ku.olga.core_impl.R
import ku.olga.core_impl.repository.OSRMDirectionsRepository
import ku.olga.core_api.repository.DirectionsRepository
import javax.inject.Singleton

@Module(includes = [DirectionsModule.BindsModule::class])
class DirectionsModule {
    @Provides
    @Reusable
    fun providesGeoApiContext(@ApplicationContext context: Context) = GeoApiContext.Builder()
        .apiKey(context.getString(R.string.google_maps_key))
        .build()

    @Module
    interface BindsModule {
        @Singleton
        @Binds
        fun bindsDirectionsRepository(repository: OSRMDirectionsRepository): DirectionsRepository
    }
}