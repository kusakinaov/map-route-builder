package ku.olga.route_builder.presentation.search.list

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.layout_error.view.*
import ku.olga.route_builder.REQ_CODE_LOCATION_PERMISSION
import ku.olga.route_builder.domain.model.SearchAddress

class SearchAddressesViewImpl(
    private val fragment: Fragment,
    private val presenter: SearchAddressesPresenter,
    private val searchAdapter: SearchAddressAdapter
) : SearchAddressesView {
    var searchView: SearchView? = null
        set(value) {
            field = value
            presenter.bindQuery()
        }

    init {
        fragment.view?.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = searchAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            buttonRetry.setOnClickListener { presenter.onClickRetry() }
        }
    }

    override fun bindAddresses(addresses: List<SearchAddress>) {
        searchAdapter.setItems(addresses)
    }

    override fun showEmpty() {
        fragment.view?.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    override fun hasLocationPermission(): Boolean {
        fragment.context?.let {
            return ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    override fun requestLocationPermission() {
        fragment.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQ_CODE_LOCATION_PERMISSION
        )
    }

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun showAddresses() {
        fragment.view?.apply {
            groupError.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
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