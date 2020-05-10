package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.route_builder.presentation.dagger.module.SearchModule
import ku.olga.route_builder.presentation.search.item.SearchAddressFragment
import ku.olga.route_builder.presentation.search.list.SearchAddressesFragment
import javax.inject.Singleton

@Component(modules = [SearchModule::class])
interface SearchComponent {
    fun inject(fragment: SearchAddressesFragment)
    fun inject(fragment: SearchAddressFragment)
}