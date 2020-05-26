package ku.olga.core_impl.dagger.component

import dagger.Component
import ku.olga.core_api.provider.AddressRepositoryProvider
import ku.olga.core_impl.dagger.module.AddressModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AddressModule::class])
interface AddressComponent : AddressRepositoryProvider