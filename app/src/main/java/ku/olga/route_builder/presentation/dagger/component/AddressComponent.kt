package ku.olga.route_builder.presentation.dagger.component

import dagger.Component
import ku.olga.route_builder.presentation.dagger.module.AddressModule
import ku.olga.route_builder.presentation.search.item.SearchAddressFragment
import ku.olga.route_builder.presentation.search.list.SearchAddressesFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AddressModule::class])
interface AddressComponent {
    fun inject(fragment: SearchAddressesFragment)
    fun inject(fragment: SearchAddressFragment)
}