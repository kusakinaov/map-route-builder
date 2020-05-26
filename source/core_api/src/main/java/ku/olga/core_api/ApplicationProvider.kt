package ku.olga.core_api

import android.content.Context
import android.content.res.AssetManager
import ku.olga.core_api.annotation.ApplicationContext

interface ApplicationProvider {
    @ApplicationContext
    fun applicationContext(): Context
    fun assetManager(): AssetManager
}