package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.route_builder.presentation.dagger.annotation.ActivityScope
import ku.olga.route_builder.presentation.dagger.module.AddressModule
import ku.olga.route_builder.presentation.dagger.module.PointModule
import ku.olga.route_builder.presentation.list.UserPointsFragment
import ku.olga.route_builder.presentation.map.UserPointsMapFragment
import ku.olga.route_builder.presentation.point.EditPointFragment
import ku.olga.route_builder.presentation.search.item.SearchAddressFragment
import ku.olga.route_builder.presentation.search.list.SearchAddressesFragment

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [AddressModule::class, PointModule::class]
)
interface ActivityComponent {
    fun inject(fragment: SearchAddressesFragment)
    fun inject(fragment: SearchAddressFragment)
    fun inject(fragment: UserPointsFragment)
    fun inject(fragment: UserPointsMapFragment)
    fun inject(fragment: EditPointFragment)
}