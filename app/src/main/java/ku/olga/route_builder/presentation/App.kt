package ku.olga.route_builder.presentation

import android.app.Application
import android.location.Geocoder
import androidx.room.Room
import ku.olga.route_builder.data.repository.PointsRepositoryImpl
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.repository.PointsRepository
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
        val pointsRepository: PointsRepository by lazy {
            PointsRepositoryImpl(
                appDatabase,
                Geocoder(application, Locale.getDefault())
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}