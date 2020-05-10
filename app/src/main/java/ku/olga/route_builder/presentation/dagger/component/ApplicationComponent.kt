package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
}