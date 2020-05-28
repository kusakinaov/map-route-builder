package ku.olga.route_builder.presentation.dagger.module

import dagger.Binds
import dagger.Module
import ku.olga.category.CategoryMediatorImpl
import ku.olga.core_api.mediator.CategoryMediator

@Module
interface MediatorsBinding {
    @Binds
    fun bindsCategoryMediator(categoryMediatorImpl: CategoryMediatorImpl): CategoryMediator
}