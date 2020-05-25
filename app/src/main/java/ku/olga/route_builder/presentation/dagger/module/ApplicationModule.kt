package ku.olga.route_builder.presentation.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ku.olga.route_builder.presentation.dagger.annotation.ApplicationContext

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    @ApplicationContext
    fun providesApplicationContext(): Context = context
}