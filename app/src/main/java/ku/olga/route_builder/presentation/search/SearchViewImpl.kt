package ku.olga.route_builder.presentation.search

import android.view.View
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchViewImpl(private val view: View) : SearchView {
    override fun showEmpty() {
        view.layoutError.visibility = View.GONE
        view.recyclerView.visibility = View.GONE
        view.textViewEmpty.visibility = View.VISIBLE
        view.progressBar.visibility = View.GONE
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