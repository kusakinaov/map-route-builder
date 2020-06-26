package ku.olga.route_builder.presentation

import android.app.Application
import ku.olga.route_builder.presentation.di.ApplicationComponent
import ku.olga.core.CoreProvidersFactory
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.ProvidersFacade
import ku.olga.route_builder.presentation.di.DaggerFacadeComponent
import ku.olga.route_builder.presentation.di.FacadeComponent

class App : Application(), AppWithFacade {
    companion object {
        lateinit var application: App
    }

    lateinit var facadeComponent: FacadeComponent

    override fun onCreate() {
        super.onCreate()
        application = this

        facadeComponent = DaggerFacadeComponent.builder()
            .applicationProvider(ApplicationComponent.get(this))
            .addressRepositoryProvider(CoreProvidersFactory.buildAddressRepositoryProvider())
            .pOIRepositoryProvider(
                CoreProvidersFactory.buildPOIRepositoryProvider(
                    ApplicationComponent.get(this)
                )
            )
            .directionsProvider(CoreProvidersFactory.buildDirectionsProvider())
            .userPointsRepositoryProvider(
                CoreProvidersFactory
                    .buildUserPointRepositoryProvider(ApplicationComponent.get(this))
            ).build()
    }

    override fun getFacade(): ProvidersFacade = facadeComponent
}
