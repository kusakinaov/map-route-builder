package ku.olga.core

import ku.olga.core_api.provider.AddressRepositoryProvider
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.core_api.provider.POIRepositoryProvider
import ku.olga.core_api.provider.UserPointsRepositoryProvider
import ku.olga.core_impl.dagger.component.DaggerAddressComponent
import ku.olga.core_impl.dagger.component.DaggerPOIComponent
import ku.olga.core_impl.dagger.component.DaggerUserPointCacheComponent

object CoreProvidersFactory {
    fun buildUserPointRepositoryProvider(applicationProvider: ApplicationProvider): UserPointsRepositoryProvider =
        DaggerUserPointCacheComponent.builder()
            .applicationProvider(applicationProvider)
            .build()

    fun buildAddressRepositoryProvider(): AddressRepositoryProvider =
        DaggerAddressComponent.builder().build()

    fun buildPOIRepositoryProvider(applicationProvider: ApplicationProvider): POIRepositoryProvider =
        DaggerPOIComponent.builder().applicationProvider(applicationProvider).build()
}