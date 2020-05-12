package ku.olga.route_builder.presentation.dagger.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.route_builder.data.repository.AddressNominatimGeocoderRepository
import ku.olga.route_builder.domain.repository.AddressRepository
import ku.olga.route_builder.presentation.dagger.annotation.ActivityScope
import org.osmdroid.bonuspack.location.GeocoderNominatim
import java.util.*

@Module
class AddressModule {
    @ActivityScope
    @Provides
    fun providesAddressRepository(geocoderNominatim: GeocoderNominatim): AddressRepository =
        AddressNominatimGeocoderRepository(geocoderNominatim)

    @Provides
    @Reusable
    fun providesGeocoderNominatim() =
        GeocoderNominatim(Locale.getDefault(), "MapRouteBuilderUserAgent")
}