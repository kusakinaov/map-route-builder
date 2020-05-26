package ku.olga.core_api.provider

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import ku.olga.core_api.annotation.ApplicationContext

interface ApplicationProvider {
    @ApplicationContext
    fun applicationContext(): Context
    fun sharedPreferences(): SharedPreferences
    fun assetManager(): AssetManager
}