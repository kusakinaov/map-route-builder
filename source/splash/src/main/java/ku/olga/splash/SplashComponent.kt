package ku.olga.splash

import dagger.Component
import ku.olga.core_api.ProvidersFacade

@Component(dependencies = [ProvidersFacade::class])
interface SplashComponent {
    fun inject(splashFragment: SplashFragment)

    companion object {
        fun build(providersFacade: ProvidersFacade): SplashComponent =
            DaggerSplashComponent.builder().providersFacade(providersFacade).build()
    }
}