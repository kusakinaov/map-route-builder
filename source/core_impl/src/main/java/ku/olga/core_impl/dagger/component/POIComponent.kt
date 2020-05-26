package ku.olga.core_impl.dagger.component

import dagger.Component
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.core_api.provider.POIRepositoryProvider
import ku.olga.core_impl.dagger.module.POIModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ApplicationProvider::class],
    modules = [POIModule::class]
)
interface POIComponent : POIRepositoryProvider