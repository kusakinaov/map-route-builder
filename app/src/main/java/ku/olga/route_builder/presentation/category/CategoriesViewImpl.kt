package ku.olga.route_builder.presentation.category

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.layout_error.view.*
import ku.olga.route_builder.domain.model.Category

class CategoriesViewImpl(
    private val fragment: Fragment,
    private val categoriesPresenter: CategoriesPresenter,
    private val categoriesAdapter: CategoriesAdapter
) : CategoriesView {

    override var searchView: SearchView? = null
        set(value) {
            field = value
            categoriesPresenter.bindQuery()
            value?.setOnQueryTextListener(buildQueryTextListener())
        }

    init {
        fragment.view?.apply {
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = categoriesAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            buttonRetry.setOnClickListener { categoriesPresenter.onClickRetry() }
        }
    }

    private fun buildQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(query: String?): Boolean {
            categoriesAdapter.setQuery(query)
            categoriesPresenter.onQueryChanged(query)
            return true
        }
    }

    override fun showProgress() {
        fragment.view?.apply {
            groupError.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        fragment.view?.progressBar?.visibility = View.GONE
    }

    override fun showError(error: CharSequence) {
        showDefaultError()
    }

    override fun showDefaultError() {
        fragment.view?.apply {
            textViewEmpty.visibility = View.GONE
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.GONE
            groupError.visibility = View.VISIBLE
        }
    }

    override fun bindCategories(categories: List<Category>) {
        categoriesAdapter.setItems(categories)
    }

    override fun showEmpty() {
        fragment.view?.apply {
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.GONE
            groupError.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun showCategories() {
        fragment.view?.apply {
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
            groupError.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun onAttach() {
        categoriesPresenter.attachView(this)
    }

    override fun onDetach() {
        categoriesPresenter.detachView()
    }
}