package ku.olga.route_builder.presentation.dagger.component

import android.content.Context
import dagger.Component
import ku.olga.route_builder.presentation.dagger.annotation.ApplicationContext
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    @ApplicationContext
    fun providesApplicationContext(): Context
}