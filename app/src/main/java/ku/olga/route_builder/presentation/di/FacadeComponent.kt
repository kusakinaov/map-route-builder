package ku.olga.route_builder.presentation.di

import dagger.Component
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.provider.*
import ku.olga.route_builder.presentation.App

@Component(
    dependencies = [
        ApplicationProvider::class,
        UserPointsRepositoryProvider::class,
        AddressRepositoryProvider::class,
        POIRepositoryProvider::class,
        DirectionsProvider::class
    ],
    modules = [MediatorsBinding::class]
)
interface FacadeComponent : ProvidersFacade