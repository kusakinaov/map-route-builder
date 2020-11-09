package ku.olga.route_builder.presentation.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import androidx.preference.PreferenceManager

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    fun providesApplicationContext(): Context = context

    @Provides
    fun providesAssetManager(context: Context): AssetManager = context.assets

    @Provides
    fun providesSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}