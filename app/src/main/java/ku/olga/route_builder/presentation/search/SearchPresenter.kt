package ku.olga.route_builder.presentation.search

import ku.olga.route_builder.presentation.base.BasePresenter

class SearchPresenter : BasePresenter<SearchView>() {
    var query: String? = null
        set(value) {
            field = value
            startSearch()
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