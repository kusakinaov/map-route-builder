package ku.olga.route_builder.presentation.search

import ku.olga.route_builder.presentation.base.BasePresenter

class SearchPresenter : BasePresenter<SearchView>() {
    var query: String? = null
        set(value) {
            field = value
            startSearch()
        }

    override fun attachView(view: SearchView) {
        super.attachView(view)
        view.apply {
            bindQuery(query)
            if (!hasLocationPermission()) {
                requestLocationPermission()
            }
        }
    }

    fun checkLocationPermission() {
        if (view?.hasLocationPermission() == true) {
            startListenToLocation()
        }
    }

    private fun startListenToLocation() {
    }

    private fun startSearch() {
        if (query?.length ?: 0 > 3) {
            showProgress()
        }
    }

    fun showEmpty() {
        view?.showEmpty()
    }
}