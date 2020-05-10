package ku.olga.route_builder.presentation.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    @Named("application_context")
    fun providesApplicationContext() = context
}