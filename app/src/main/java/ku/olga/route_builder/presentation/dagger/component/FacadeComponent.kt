package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.ApplicationProvider
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.UserPointsRepositoryProvider
import ku.olga.route_builder.presentation.App

@Component(
    dependencies = [ApplicationProvider::class, UserPointsRepositoryProvider::class]
)
interface FacadeComponent : ProvidersFacade {
    fun inject(app: App)
}