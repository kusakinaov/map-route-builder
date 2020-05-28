package ku.olga.route_builder.presentation.dagger.module

import dagger.Binds
import dagger.Module
import ku.olga.category.CategoryMediatorImpl
import ku.olga.core_api.mediator.CategoryMediator
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.core_api.mediator.SearchMediator
import ku.olga.edit_point.EditPointMediatorImpl
import ku.olga.search.SearchMediatorImpl

@Module
interface MediatorsBinding {
    @Binds
    fun bindsCategoryMediator(categoryMediatorImpl: CategoryMediatorImpl): CategoryMediator

    @Binds
    fun bindEditPointMediator(editPointMediatorImpl: EditPointMediatorImpl): EditPointMediator

    @Binds
    fun bindSearchMediator(searchMediatorImpl: SearchMediatorImpl): SearchMediator
}