package ku.olga.edit_point

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(
    dependencies = [ProvidersFacade::class]
)
interface EditPointComponent {
    fun inject(fragment: EditPointFragment)

    companion object {
        fun build(providersFacade: ProvidersFacade): EditPointComponent =
            DaggerEditPointComponent.builder().providersFacade(providersFacade).build()
    }
}