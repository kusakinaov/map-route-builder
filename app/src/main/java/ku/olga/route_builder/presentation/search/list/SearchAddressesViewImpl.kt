package ku.olga.route_builder.presentation.search.list

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.view.*
import ku.olga.route_builder.REQ_CODE_LOCATION_PERMISSION
import ku.olga.route_builder.domain.model.SearchAddress

class SearchAddressesViewImpl(
    private val fragment: Fragment,
    private val presenter: SearchAddressesPresenter,
    private val searchAdapter: SearchAddressAdapter,
    private val view: View
) : SearchAddressesView {
    var searchView: SearchView? = null
        set(value) {
            field = value
            presenter.bindQuery()
        }

    init {
        view.recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = searchAdapter
        }
        view.buttonRetry.setOnClickListener { presenter.onClickRetry() }
    }

    override fun showEmpty() {
        view.layoutError.visibility = View.GONE
        view.recyclerView.visibility = View.GONE
        view.textViewEmpty.visibility = View.VISIBLE
        view.progressBar.visibility = View.GONE
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

    override fun showAddresses(addresses: List<SearchAddress>) {
        searchAdapter.setItems(addresses)
        view.layoutError.visibility = View.GONE
        view.recyclerView.visibility = View.VISIBLE
        view.textViewEmpty.visibility = View.GONE
        view.progressBar.visibility = View.GONE
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }

    override fun showProgress() {
        view.layoutError.visibility = View.GONE
        view.recyclerView.visibility = View.GONE
        view.textViewEmpty.visibility = View.GONE
        view.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        view.progressBar.visibility = View.GONE
    }

    override fun showError(error: CharSequence) {
        showDefaultError()
    }

    override fun showDefaultError() {
        view.recyclerView.visibility = View.GONE
        view.textViewEmpty.visibility = View.GONE
        view.progressBar.visibility = View.GONE
        view.layoutError.visibility = View.VISIBLE
    }
}