package ku.olga.route_builder.presentation

import android.app.Application
import android.location.Geocoder
import ku.olga.route_builder.data.repository.PointsRepositoryImpl
import ku.olga.route_builder.domain.repository.PointsRepository
import java.util.*

class App : Application() {
    companion object {
        lateinit var application: App
        val pointsRepository: PointsRepository
                by lazy { PointsRepositoryImpl(Geocoder(application, Locale.getDefault())) }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}