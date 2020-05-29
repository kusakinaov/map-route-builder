package ku.olga.route_builder.presentation.di

import dagger.Component
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.provider.AddressRepositoryProvider
import ku.olga.core_api.provider.POIRepositoryProvider
import ku.olga.core_api.provider.UserPointsRepositoryProvider
import ku.olga.route_builder.presentation.App

@Component(
        dependencies = [
            ApplicationProvider::class,
            UserPointsRepositoryProvider::class,
            AddressRepositoryProvider::class,
            POIRepositoryProvider::class
        ],
        modules = [MediatorsBinding::class]
)
interface FacadeComponent : ProvidersFacade