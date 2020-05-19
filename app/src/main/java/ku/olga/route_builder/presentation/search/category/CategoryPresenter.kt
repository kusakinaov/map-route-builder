package ku.olga.route_builder.presentation.search.category

import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.presentation.base.BasePresenter

class CategoryPresenter : BasePresenter<CategoryView>() {
    var category: Category? = null
}