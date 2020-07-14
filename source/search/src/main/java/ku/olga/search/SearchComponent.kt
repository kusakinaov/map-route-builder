package ku.olga.search

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(dependencies = [ProvidersFacade::class])
interface SearchComponent {
    companion object {
        fun build(providersFacade: ProvidersFacade): SearchComponent =
            DaggerSearchComponent.builder().providersFacade(providersFacade).build()
    }

    fun inject(fragment: SearchAddressesFragment)
    fun inject(fragment: SearchMapFragment)

}