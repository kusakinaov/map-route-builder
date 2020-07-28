package ku.olga.core_impl.dagger.module

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import ku.olga.core_api.repository.POIRepository
import ku.olga.core_impl.repository.NominatimPOIRepository
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import javax.inject.Singleton

@Module
class POIModule {
    @Provides
    fun providesGson() = GsonBuilder().create()

    @Provides
    fun providesPOIProvider(): NominatimPOIProvider = NominatimPOIProvider(USER_AGENT)

    @Provides
    @Singleton
    fun providesPOIRepository(assetManager: AssetManager, gson: Gson): POIRepository =
        NominatimPOIRepository(assetManager, gson)

    companion object {
        private const val USER_AGENT = "MapRouteBuilderUserAgent"
    }
}