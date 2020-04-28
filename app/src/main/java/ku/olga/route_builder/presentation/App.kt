package ku.olga.route_builder.presentation

import android.app.Application
import android.location.Geocoder
import androidx.room.Room
import ku.olga.route_builder.data.repository.PointsRepositoryImpl
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.domain.services.PointsService
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
                PointsRepositoryImpl(
                    appDatabase,
                    Geocoder(application, Locale.getDefault())
                )
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}