package ku.olga.route_builder.presentation.dagger.module

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import ku.olga.route_builder.data.repository.CategoryNominatimRepository
import ku.olga.route_builder.data.repository.NominatimPOIRepository
import ku.olga.route_builder.domain.repository.CategoryRepository
import ku.olga.route_builder.domain.repository.POIRepository
import ku.olga.route_builder.presentation.dagger.annotation.ActivityScope
import org.osmdroid.bonuspack.location.NominatimPOIProvider

@Module
class POIModule {
    @Provides
    @ActivityScope
    fun providesGson() = GsonBuilder().create()

    @Provides
    @ActivityScope
    fun providesCategoryRepository(assetManager: AssetManager, gson: Gson): CategoryRepository =
        CategoryNominatimRepository(assetManager, gson)

    @Provides
    fun providesPOIProvider(): NominatimPOIProvider = NominatimPOIProvider(USER_AGENT)

    @Provides
    @ActivityScope
    fun providesPOIRepository(nominatimPOIProvider: NominatimPOIProvider): POIRepository =
        NominatimPOIRepository(nominatimPOIProvider)

    companion object {
        private const val USER_AGENT = "MapRouteBuilderUserAgent"
    }
}