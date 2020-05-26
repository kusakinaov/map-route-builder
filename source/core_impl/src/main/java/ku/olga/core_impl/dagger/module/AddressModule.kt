package ku.olga.core_impl.dagger.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ku.olga.core_api.repository.AddressRepository
import ku.olga.core_impl.repository.AddressNominatimGeocoderRepository
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
        @Binds
        fun providesAddressRepository(repository: AddressNominatimGeocoderRepository): AddressRepository
    }
}