package ku.olga.core_api.provider

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager

interface ApplicationProvider {
    fun applicationContext(): Context
    fun sharedPreferences(): SharedPreferences
    fun assetManager(): AssetManager
}