package ku.olga.core_api.mediator

interface MediatorsProvider {
    fun providesCategoryMediator(): CategoryMediator
}