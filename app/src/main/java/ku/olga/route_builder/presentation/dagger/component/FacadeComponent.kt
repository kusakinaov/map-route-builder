package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.AppDatabaseProvider
import ku.olga.core_api.ApplicationProvider
import ku.olga.core_api.ProvidersFacade

@Component(
    dependencies = [ApplicationProvider::class, AppDatabaseProvider::class]
)
interface FacadeComponent : ProvidersFacade