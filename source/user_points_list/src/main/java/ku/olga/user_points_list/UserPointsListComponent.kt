package ku.olga.user_points_list

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(dependencies = [ProvidersFacade::class])
interface UserPointsListComponent {
    fun inject(fragment: UserPointsListFragment)

    companion object {
        fun build(providersFacade: ProvidersFacade): UserPointsListComponent =
            DaggerUserPointsListComponent.builder().providersFacade(providersFacade).build()
    }
}