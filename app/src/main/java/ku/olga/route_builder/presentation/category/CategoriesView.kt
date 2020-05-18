package ku.olga.route_builder.presentation.category

import ku.olga.route_builder.domain.model.Category

interface CategoriesView {
    fun bindCategories(addresses: List<Category>)
    fun showEmpty()
    fun showCategories()
    fun bindQuery(query: String?)
}