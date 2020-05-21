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
import ku.olga.route_builder.domain.model.Coordinates
import org.osmdroid.bonuspack.location.GeocoderNominatim
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.config.Configuration
import java.util.Locale

class App : Application() {
    companion object {
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val DEFAULT_LATITUDE = 54.180857
        private const val DEFAULT_LONGITUDE = 45.186319
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

    fun getLastCoordinates() = Coordinates(preferences.getDouble(LATITUDE, DEFAULT_LATITUDE),
            preferences.getDouble(LONGITUDE, DEFAULT_LONGITUDE))

    fun setLastCoordinates(coordinates: Coordinates) {
        preferences.edit().putDouble(LATITUDE, coordinates.latitude).putDouble(LONGITUDE, coordinates.longitude).apply()
    }

    private fun SharedPreferences.Editor.putDouble(key: String, double: Double): SharedPreferences.Editor =
            putLong(key, java.lang.Double.doubleToRawLongBits(double))

    private fun SharedPreferences.getDouble(key: String, defaultDouble: Double): Double =
            java.lang.Double.longBitsToDouble(getLong(key,
                    java.lang.Double.doubleToRawLongBits(defaultDouble)))
}
