package ku.olga.route_builder.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.gson.GsonBuilder
import ku.olga.route_builder.data.repository.AddressNominatimGeocoderRepository
import ku.olga.route_builder.data.repository.CategoryNominatimRepository
import ku.olga.route_builder.data.repository.NominatimPOIRepository
import ku.olga.route_builder.data.repository.PointsDbCacheRepository
import ku.olga.route_builder.data.room.AppDatabase
import org.osmdroid.bonuspack.location.GeocoderNominatim
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.config.Configuration
import java.util.Locale

class App : Application() {
    companion object {
        private const val USER_AGENT = "MapRouteBuilderUserAgent"
        lateinit var application: App
        private val appDatabase: AppDatabase by lazy {
            Room.databaseBuilder(application, AppDatabase::class.java, "route-builder-database")
                .fallbackToDestructiveMigration().build()
        }
        val addressRepository by lazy {
            AddressNominatimGeocoderRepository(GeocoderNominatim(Locale.ROOT, USER_AGENT))
        }
        val pointsRepository by lazy { PointsDbCacheRepository(appDatabase) }
        val categoriesRepository by lazy {
            CategoryNominatimRepository(application.assets, GsonBuilder().create())
        }
        val poiRepository by lazy { NominatimPOIRepository(NominatimPOIProvider(USER_AGENT)) }
        val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(application) }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        Configuration.getInstance().load(this, preferences);
    }
}
