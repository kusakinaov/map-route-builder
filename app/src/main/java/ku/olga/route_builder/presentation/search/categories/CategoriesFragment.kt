package ku.olga.route_builder.presentation.search.categories

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_VIEW_CATEGORY
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.search.category.CategoryFragment

class CategoriesFragment : BaseFragment() {
    private val categoriesAdapter = CategoriesAdapter().apply {
        categoryClickListener = {
            replaceFragment(CategoryFragment
                    .newInstance(this@CategoriesFragment, REQ_CODE_VIEW_CATEGORY, it), true)
        }
    }
    private var categoriesView: CategoriesView? = null
    private val categoriesPresenter = CategoriesPresenter(App.categoriesRepository)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesView = CategoriesViewImpl(this, categoriesPresenter, categoriesAdapter)
        categoriesView?.onAttach()
    }

    override fun onDestroyView() {
        categoriesView?.onDetach()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.categories, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val view = menu.findItem(R.id.actionSearch)?.actionView
        if (view is SearchView) {
            categoriesView?.searchView = view
            view.queryHint = getString(R.string.hint_search_category)
        }
    }

    companion object {
        fun newInstance() = CategoriesFragment()
    }
}