package ku.olga.core_api

import ku.olga.core_api.mediator.MediatorsProvider
import ku.olga.core_api.provider.*

interface ProvidersFacade : ApplicationProvider, UserPointsRepositoryProvider,
        AddressRepositoryProvider, POIRepositoryProvider, MediatorsProvider, DirectionsProvider