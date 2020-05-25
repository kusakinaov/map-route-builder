package ku.olga.route_builder.presentation

import android.app.Application
import ku.olga.route_builder.presentation.dagger.component.ApplicationComponent
import ku.olga.route_builder.presentation.dagger.component.DaggerApplicationComponent
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule

class App : Application() {
    companion object {
        lateinit var application: App
    }

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        application = this
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}