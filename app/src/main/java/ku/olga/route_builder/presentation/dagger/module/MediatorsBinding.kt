package ku.olga.route_builder.presentation.dagger.module

import dagger.Binds
import dagger.Module
import ku.olga.category.CategoryMediatorImpl
import ku.olga.confirmation.ConfirmationMediatorImpl
import ku.olga.core_api.mediator.*
import ku.olga.edit_point.EditPointMediatorImpl
import ku.olga.search.SearchMediatorImpl
import ku.olga.user_points.root.UserPointsMediatorImpl

@Module
interface MediatorsBinding {
    @Binds
    fun bindsCategoryMediator(categoryMediatorImpl: CategoryMediatorImpl): CategoryMediator

    @Binds
    fun bindEditPointMediator(editPointMediatorImpl: EditPointMediatorImpl): EditPointMediator

    @Binds
    fun bindSearchMediator(searchMediatorImpl: SearchMediatorImpl): SearchMediator

    @Binds
    fun bindConfirmationMediator(confirmationMediatorImpl: ConfirmationMediatorImpl): ConfirmationMediator

    @Binds
    fun bindUserPointsMediator(userPointsMediatorImpl: UserPointsMediatorImpl): UserPointsMediator
}