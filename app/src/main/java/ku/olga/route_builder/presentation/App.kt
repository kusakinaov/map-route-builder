package ku.olga.route_builder.presentation

import android.app.Application
import ku.olga.route_builder.presentation.dagger.component.ApplicationComponent
import android.content.SharedPreferences
import ku.olga.core.CoreProvidersFactory
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.ProvidersFacade
import ku.olga.route_builder.presentation.dagger.component.DaggerFacadeComponent
import ku.olga.route_builder.presentation.dagger.component.FacadeComponent
import org.osmdroid.config.Configuration
import javax.inject.Inject

class App : Application(), AppWithFacade {
    companion object {
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

    override fun getFacade(): ProvidersFacade = facadeComponent
}
