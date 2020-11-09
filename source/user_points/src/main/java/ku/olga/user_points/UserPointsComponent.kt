package ku.olga.user_points

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(dependencies = [ProvidersFacade::class])
interface UserPointsComponent {
    fun inject(fragment: UserPointsFragment)

    companion object {
        fun build(providersFacade: ProvidersFacade): UserPointsComponent =
            DaggerUserPointsComponent.builder().providersFacade(providersFacade).build()
    }
}