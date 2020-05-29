package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.annotation.ActivityScope
import ku.olga.edit_point.EditPointFragment
import ku.olga.user_points.list.UserPointsListFragment
import ku.olga.user_points.map.UserPointsMapFragment
import ku.olga.route_builder.presentation.user_points.root.UserPointsFragment

@ActivityScope
@Component(dependencies = [FacadeComponent::class])
interface ActivityComponent {
    fun inject(fragment: ku.olga.search.SearchAddressesFragment)
    fun inject(fragment: UserPointsFragment)
    fun inject(fragment: UserPointsMapFragment)
    fun inject(fragment: EditPointFragment)
    fun inject(fragment: ku.olga.category.CategoryFragment)
    fun inject(fragment: UserPointsListFragment)
}