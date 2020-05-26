package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.core_api.annotation.ActivityScope
import ku.olga.route_builder.presentation.dagger.module.AddressModule
import ku.olga.route_builder.presentation.dagger.module.POIModule
import ku.olga.core_impl.dagger.UserPointCacheModule
import ku.olga.route_builder.presentation.point.EditPointFragment
import ku.olga.route_builder.presentation.search.category.CategoryFragment
import ku.olga.route_builder.presentation.search.list.SearchAddressesFragment
import ku.olga.route_builder.presentation.user_points.list.UserPointsListFragment
import ku.olga.route_builder.presentation.user_points.map.UserPointsMapFragment
import ku.olga.route_builder.presentation.user_points.root.UserPointsFragment

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [AddressModule::class, UserPointCacheModule::class, POIModule::class]
)
interface ActivityComponent {
    fun inject(fragment: SearchAddressesFragment)
    fun inject(fragment: UserPointsFragment)
    fun inject(fragment: UserPointsMapFragment)
    fun inject(fragment: EditPointFragment)
    fun inject(fragment: CategoryFragment)
    fun inject(fragment: UserPointsListFragment)
}