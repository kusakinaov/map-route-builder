package ku.olga.core_impl.dagger.component

import dagger.Component
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.core_api.provider.UserPointsRepositoryProvider
import ku.olga.core_impl.dagger.module.UserPointCacheModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ApplicationProvider::class],
    modules = [UserPointCacheModule::class]
)
interface UserPointCacheComponent : UserPointsRepositoryProvider