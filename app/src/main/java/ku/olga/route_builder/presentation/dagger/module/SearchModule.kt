package ku.olga.route_builder.presentation.dagger.module

import dagger.Module
import dagger.Provides
import ku.olga.route_builder.data.repository.AddressNominatimGeocoderRepository
import ku.olga.route_builder.domain.repository.AddressRepository
import org.osmdroid.bonuspack.location.GeocoderNominatim
import java.util.*
import javax.inject.Singleton

@Module
class SearchModule {
    @Singleton
    @Provides
    fun providesAddressRepository(geocoderNominatim: GeocoderNominatim): AddressRepository =
        AddressNominatimGeocoderRepository(geocoderNominatim)

    @Provides
    fun providesGeocoderNominatim() =
        GeocoderNominatim(Locale.getDefault(), "MapRouteBuilderUserAgent")
}