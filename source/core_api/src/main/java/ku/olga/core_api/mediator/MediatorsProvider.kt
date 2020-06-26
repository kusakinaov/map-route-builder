package ku.olga.core_api.mediator

interface MediatorsProvider {
    fun providesCategoryMediator(): CategoryMediator
    fun providesEditPointMediator(): EditPointMediator
    fun providesSearchMediator(): SearchMediator
    fun providesConfirmationMediator(): ConfirmationMediator
    fun providesUserPointsMediator(): UserPointsMediator
    fun providesMainMediator(): MainMediator
}