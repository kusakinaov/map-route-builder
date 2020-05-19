package ku.olga.route_builder.presentation.search.categories

import androidx.appcompat.widget.SearchView
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.presentation.base.BaseView

interface CategoriesView : BaseView {
    var searchView: SearchView?
    fun bindCategories(categories: List<Category>)
    fun showEmpty()
    fun showCategories()
    fun bindQuery(query: String?)
}