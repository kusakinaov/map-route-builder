package ku.olga.route_builder.presentation.search

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_search.view.*
import ku.olga.route_builder.REQ_CODE_LOCATION_PERMISSION

class SearchViewImpl(private val fragment: Fragment, private val view: View) : SearchView {
    override fun showEmpty() {
        view.layoutError.visibility = View.GONE
        view.recyclerView.visibility = View.GONE
        view.textViewEmpty.visibility = View.VISIBLE
        view.progressBar.visibility = View.GONE
    }

    override fun hasLocationPermission(): Boolean {
        fragment.context?.let {
            return ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    override fun requestLocationPermission() {
        fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQ_CODE_LOCATION_PERMISSION)
    }

    override fun bindQuery(query: String?) {
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
        view.recyclerView.visibility = View.GONE
        view.textViewEmpty.visibility = View.GONE
        view.progressBar.visibility = View.GONE
        view.layoutError.visibility = View.VISIBLE
    }
}