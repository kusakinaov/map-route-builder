package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.route_builder.presentation.dagger.module.PointModule
import ku.olga.route_builder.presentation.list.UserPointsFragment
import ku.olga.route_builder.presentation.map.UserPointsMapFragment
import ku.olga.route_builder.presentation.point.EditPointFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [PointModule::class])
interface PointComponent {
    fun inject(fragment: UserPointsFragment)
    fun inject(fragment: UserPointsMapFragment)
    fun inject(fragment: EditPointFragment)
}