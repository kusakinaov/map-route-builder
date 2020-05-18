package ku.olga.route_builder.presentation.category

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment

class CategoriesFragment : BaseFragment() {
    val categoriesAdapter = CategoriesAdapter().apply {
        categoryClickListener = {
            //todo
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_category, container, false)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.categories, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val view = menu.findItem(R.id.actionSearch)?.actionView
        if (view is SearchView) {
            view.apply {
                isIconified = false
                queryHint = getString(R.string.hint_search_category)
                setOnQueryTextListener(buildOnQueryTextListener())
            }
        }
    }

    private fun buildOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            //todo
//            onQueryChanged(newText)
            return true
        }
    }

    companion object {
        fun newInstance() = CategoriesFragment()
    }
}