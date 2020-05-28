package ku.olga.category

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(
    dependencies = [ProvidersFacade::class]
)
interface CategoryComponent {
    fun inject(fragment: CategoryFragment)

    companion object {
        fun build(providersFacade: ProvidersFacade): CategoryComponent =
            DaggerCategoryComponent.builder().providersFacade(providersFacade).build()
    }
}