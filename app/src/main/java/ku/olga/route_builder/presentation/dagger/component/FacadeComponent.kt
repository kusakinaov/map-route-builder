package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.provider.AddressRepositoryProvider
import ku.olga.core_api.provider.UserPointsRepositoryProvider
import ku.olga.core_api.repository.AddressRepository
import ku.olga.route_builder.presentation.App

@Component(
    dependencies = [
        ApplicationProvider::class,
        UserPointsRepositoryProvider::class,
        AddressRepositoryProvider::class
    ]
)
interface FacadeComponent : ProvidersFacade {
    fun inject(app: App)
}