package ku.olga.main

import dagger.Component
import ku.olga.core_api.ProvidersFacade
import ku.olga.core_api.annotation.ActivityScope

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