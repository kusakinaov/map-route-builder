package ku.olga.route_builder.presentation

import android.app.Application
import android.location.Geocoder
import androidx.room.Room
import ku.olga.route_builder.data.repository.AddressGeocoderRepository
import ku.olga.route_builder.data.repository.PointsDbCacheRepository
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.services.PointsServiceImpl
import java.util.Locale

class App : Application() {
    companion object {
        lateinit var application: App
        private val appDatabase: AppDatabase by lazy {
            Room
                .databaseBuilder(application, AppDatabase::class.java, "route-builder-database")
                .fallbackToDestructiveMigration()
                .build()
        }
        val pointsService by lazy {
            PointsServiceImpl(
                AddressGeocoderRepository(Geocoder(application, Locale.getDefault())),
                PointsDbCacheRepository(appDatabase)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}