package ku.olga.core_api

import ku.olga.core_api.provider.AddressRepositoryProvider
import ku.olga.core_api.provider.ApplicationProvider
import ku.olga.core_api.provider.UserPointsRepositoryProvider

interface ProvidersFacade : ApplicationProvider, UserPointsRepositoryProvider, AddressRepositoryProvider