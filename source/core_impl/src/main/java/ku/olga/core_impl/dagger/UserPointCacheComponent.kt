package ku.olga.core_impl.dagger

import dagger.Component
import ku.olga.core_api.ApplicationProvider
import ku.olga.core_api.UserPointsRepositoryProvider

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [UserPointCacheModule::class]
)
interface UserPointCacheComponent : UserPointsRepositoryProvider