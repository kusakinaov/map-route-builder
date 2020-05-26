package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.ApplicationProvider
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : ApplicationProvider {
    fun inject(app: App)
}