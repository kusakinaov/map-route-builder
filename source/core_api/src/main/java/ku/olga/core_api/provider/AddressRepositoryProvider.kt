package ku.olga.core_api.provider

import ku.olga.core_api.repository.AddressRepository

interface AddressRepositoryProvider {
    fun addressRepository(): AddressRepository
}