package ku.olga.route_builder.presentation.dagger.module

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import ku.olga.route_builder.presentation.dagger.annotation.ApplicationContext
import androidx.preference.PreferenceManager

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    @ApplicationContext
    fun providesApplicationContext(): Context = context

    @Provides
    fun providesAssetManager(@ApplicationContext context: Context): AssetManager = context.assets

    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}