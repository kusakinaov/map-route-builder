package ku.olga.search

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.layout_error.view.*
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress

class SearchAddressesViewImpl(
    private val fragment: Fragment,
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
        fragment.view?.apply {
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
        fragment.view?.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    override fun hasLocationPermission(): Boolean =
        ku.olga.ui_core.utils.hasLocationPermission(fragment.context)

    override fun requestLocationPermission() {
        ku.olga.ui_core.utils.requestLocationPermission(fragment, REQ_CODE_LOCATION_PERMISSION)
    }

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun showAddresses() {
        fragment.view?.apply {
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
        fragment.view?.apply {
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
        fragment.view?.apply {
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
        fragment.view?.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
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
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
            groupError.visibility = View.VISIBLE
        }
    }
}