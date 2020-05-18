package ku.olga.route_builder.presentation.category

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment

class CategoriesFragment : BaseFragment() {
    private val categoriesAdapter = CategoriesAdapter().apply {
        categoryClickListener = {
            //todo
        }
    }
    private var categoriesView: CategoriesView? = null
    private val сategoriesPresenter = CategoriesPresenter(App.categoriesRepository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesView = CategoriesViewImpl(this, сategoriesPresenter, categoriesAdapter)
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
            view.apply {
//                isIconified = false
                queryHint = getString(R.string.hint_search_category)
            }
        }
    }

    companion object {
        fun newInstance() = CategoriesFragment()
    }
}