package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.annotation.ActivityScope
import ku.olga.edit_point.EditPointFragment
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.user_points.list.UserPointsListFragment
import ku.olga.user_points.map.UserPointsMapFragment
import ku.olga.user_points.root.UserPointsFragment

@ActivityScope
@Component(dependencies = [ProvidersFacade::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)

    companion object {
        fun build(providersFacade: ProvidersFacade): ActivityComponent =
            DaggerActivityComponent.builder()
                .providersFacade(providersFacade)
                .build()
    }
}