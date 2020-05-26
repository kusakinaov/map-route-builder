package ku.olga.route_builder.presentation.dagger.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.route_builder.data.repository.AddressNominatimGeocoderRepository
import ku.olga.route_builder.domain.repository.AddressRepository
import ku.olga.core_api.annotation.ActivityScope
import org.osmdroid.bonuspack.location.GeocoderNominatim
import java.util.Locale

@Module(includes = [AddressModule.BindsModule::class])
class AddressModule {
    @Provides
    @Reusable
    fun providesGeocoderNominatim() =
        GeocoderNominatim(Locale.getDefault(), "MapRouteBuilderUserAgent")

    @Module
    interface BindsModule {
        @ActivityScope
        @Binds
        fun providesAddressRepository(repository: AddressNominatimGeocoderRepository): AddressRepository
    }
}