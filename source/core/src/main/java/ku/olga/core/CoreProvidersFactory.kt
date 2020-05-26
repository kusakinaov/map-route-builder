package ku.olga.core

import ku.olga.core_api.ApplicationProvider
import ku.olga.core_api.UserPointsRepositoryProvider
import ku.olga.core_impl.dagger.DaggerUserPointCacheComponent

object CoreProvidersFactory {
    fun buildUserPointRepositoryProvider(applicationProvider: ApplicationProvider): UserPointsRepositoryProvider =
        DaggerUserPointCacheComponent.builder()
            .applicationProvider(applicationProvider)
            .build()
}