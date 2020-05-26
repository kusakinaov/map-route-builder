package ku.olga.core_impl.dagger.module

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import ku.olga.core_api.repository.POIRepository
import ku.olga.core_impl.repository.NominatimPOIRepository
import org.osmdroid.bonuspack.location.NominatimPOIProvider

@Module
class POIModule {
    @Provides
    fun providesGson() = GsonBuilder().create()
    
    @Provides
    fun providesPOIProvider(): NominatimPOIProvider = NominatimPOIProvider(USER_AGENT)

    @Provides
    fun providesPOIRepository(
        assetManager: AssetManager,
        gson: Gson,
        nominatimPOIProvider: NominatimPOIProvider
    ): POIRepository =
        NominatimPOIRepository(assetManager, gson, nominatimPOIProvider)

    companion object {
        private const val USER_AGENT = "MapRouteBuilderUserAgent"
    }
}