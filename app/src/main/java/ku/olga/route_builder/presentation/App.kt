package ku.olga.route_builder.presentation

import android.app.Application
import ku.olga.route_builder.presentation.dagger.component.ApplicationComponent
import android.content.SharedPreferences
import ku.olga.core.CoreProvidersFactory
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.dto.Coordinates
import ku.olga.route_builder.presentation.dagger.component.DaggerFacadeComponent
import ku.olga.route_builder.presentation.dagger.component.FacadeComponent
import org.osmdroid.config.Configuration
import javax.inject.Inject

class App : Application(), AppWithFacade {
    companion object {
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val DEFAULT_LATITUDE = 54.180857
        private const val DEFAULT_LONGITUDE = 45.186319

        lateinit var application: App
    }

    lateinit var facadeComponent: FacadeComponent

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        application = this

        facadeComponent = DaggerFacadeComponent.builder()
            .applicationProvider(ApplicationComponent.get(this))
            .addressRepositoryProvider(CoreProvidersFactory.buildAddressRepositoryProvider())
            .pOIRepositoryProvider(CoreProvidersFactory.buildPOIRepositoryProvider(ApplicationComponent.get(this)))
            .userPointsRepositoryProvider(
                CoreProvidersFactory
                    .buildUserPointRepositoryProvider(ApplicationComponent.get(this))
            ).build()
        facadeComponent.inject(this)

        Configuration.getInstance().load(this, preferences)
    }

    fun getLastCoordinates() = Coordinates(
        preferences.getDouble(LATITUDE, DEFAULT_LATITUDE),
        preferences.getDouble(LONGITUDE, DEFAULT_LONGITUDE)
    )

    fun setLastCoordinates(coordinates: Coordinates) {
        preferences.edit().putDouble(LATITUDE, coordinates.latitude)
            .putDouble(LONGITUDE, coordinates.longitude).apply()
    }

    private fun SharedPreferences.Editor.putDouble(
        key: String,
        double: Double
    ): SharedPreferences.Editor =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    private fun SharedPreferences.getDouble(key: String, defaultDouble: Double): Double =
        java.lang.Double.longBitsToDouble(
            getLong(
                key,
                java.lang.Double.doubleToRawLongBits(defaultDouble)
            )
        )

    override fun getFacade(): ProvidersFacade = facadeComponent
}
