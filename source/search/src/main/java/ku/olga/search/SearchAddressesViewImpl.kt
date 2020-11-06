package ku.olga.search

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.layout_error.view.*
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress

abstract class SearchAddressesViewImpl(
    private val view: View,
    private val presenter: SearchAddressesPresenter,
    private val addressesAdapter: AddressesAdapter,
    private val categoriesAdapter: CategoriesAdapter
) : SearchAddressesView {
    override var searchView: SearchView? = null
        set(value) {
            field = value
            presenter.bindQuery()
            value?.apply {
                queryHint = context.getString(R.string.hint_search_address)
                setOnQueryTextListener(buildOnQueryTextListener())
            }
        }

    private fun buildOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            presenter.onQueryChanged(newText)
            addressesAdapter.setQuery(newText)
            return true
        }
    }

    init {
        view.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addressesAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            buttonRetry.setOnClickListener { presenter.onClickRetry() }
        }
    }

    override fun bindAddresses(addresses: List<SearchAddress>) {
        addressesAdapter.setItems(addresses)
    }

    override fun showEmpty() {
        view.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    override fun hasLocationPermission(): Boolean =
        ku.olga.ui_core.utils.hasLocationPermission(view.context)

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun showAddresses() {
        view.apply {
            groupError.visibility = View.GONE
            recyclerView.apply {
                visibility = View.VISIBLE
                adapter = addressesAdapter
            }
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    override fun bindCategories(categories: List<Category>) {
        categoriesAdapter.setItems(categories)
    }

    override fun showCategories() {
        view.apply {
            groupError.visibility = View.GONE
            recyclerView.apply {
                visibility = View.VISIBLE
                adapter = categoriesAdapter
            }
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    override fun showNoSearch() {
        view.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }

    override fun showProgress() {
        view.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        view.progressBar?.visibility = View.GONE
    }

    override fun showError(error: CharSequence) {
        showDefaultError()
    }

    override fun showDefaultError() {
        view.apply {
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
            groupError.visibility = View.VISIBLE
        }
    }
}