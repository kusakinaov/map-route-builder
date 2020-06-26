package ku.olga.core_impl.dagger.component

import dagger.Component
import ku.olga.core_api.provider.DirectionsProvider
import ku.olga.core_impl.dagger.module.DirectionsModule
import javax.inject.Singleton

@Singleton
@Component(modules = [DirectionsModule::class])
interface DirectionsComponent : DirectionsProvider