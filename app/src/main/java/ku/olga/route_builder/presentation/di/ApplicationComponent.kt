package ku.olga.route_builder.presentation.di

import dagger.Component
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.route_builder.presentation.App

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : ApplicationProvider {
    companion object {
        private var applicationComponent: ApplicationProvider? = null

        fun get(application: App): ApplicationProvider =
            applicationComponent
                ?: DaggerApplicationComponent.builder()
                    .applicationModule(
                        ApplicationModule(
                            application
                        )
                    ).build().also { applicationComponent = it }
    }
}