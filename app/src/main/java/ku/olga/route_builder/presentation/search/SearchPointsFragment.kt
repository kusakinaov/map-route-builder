package ku.olga.route_builder.presentation.search

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_search.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment

class SearchPointsFragment : BaseFragment() {
    private val searchPresenter = SearchPresenter()
    private var locationClient: FusedLocationProviderClient? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_search)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
        }

        searchPresenter.attachView(SearchViewImpl(this, view))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        searchPresenter.checkLocationPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchPresenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_points, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val view = menu.findItem(R.id.actionSearch)?.actionView
        if (view is SearchView) {
            view.apply {
                isIconified = false
                queryHint = getString(R.string.hint_search_points)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?) = true

                    override fun onQueryTextChange(newText: String?): Boolean {
                        onQueryChanged(newText)
                        return true
                    }
                })
            }
        }
    }

    fun onQueryChanged(query: String?) {
        searchPresenter.query = query
    }

    companion object {
        fun newInstance(target: Fragment, requestCode: Int) =
                SearchPointsFragment().apply { setTargetFragment(target, requestCode) }
    }
}