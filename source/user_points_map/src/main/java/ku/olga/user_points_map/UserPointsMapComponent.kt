package ku.olga.user_points_map

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(dependencies = [ProvidersFacade::class])
interface UserPointsMapComponent {
    fun inject(fragment: UserPointsMapFragment)

    companion object {
        fun build(providersFacade: ProvidersFacade): UserPointsMapComponent =
            DaggerUserPointsMapComponent.builder().providersFacade(providersFacade).build()
    }
}