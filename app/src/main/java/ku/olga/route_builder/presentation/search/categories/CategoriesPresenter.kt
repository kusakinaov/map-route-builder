package ku.olga.route_builder.presentation.search.categories

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.repository.CategoryRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import java.io.IOException

class CategoriesPresenter(val repository: CategoryRepository) : BasePresenter<CategoriesView>() {
    private var query: String = ""
    private val categories = mutableListOf<Category>()
    private var job: Job? = null

    override fun attachView(view: CategoriesView) {
        super.attachView(view)
        bindQuery()
        bindCategories()
        if (categories.isEmpty() && query.isEmpty()) {
            trySearch()
        }
    }

    fun onQueryChanged(query: String?) {
        this.query = query ?: ""
        trySearch()
    }

    fun bindQuery() {
        view?.bindQuery(query)
    }

    private fun trySearch() {
        job?.let { if (it.isActive) it.cancel() }
        job = runSearch()
    }

    private fun runSearch() = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Dispatchers.Main) { showProgress() }
            val categories = repository.getCategories(query)
            withContext(Dispatchers.Main) { setCategories(categories) }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
    }

    private fun setCategories(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        bindCategories()
    }

    private fun bindCategories() {
        view?.apply {
            bindCategories(categories)
            if (categories.isEmpty()) {
                showEmpty()
            } else {
                showCategories()
            }
        }
    }

    fun onClickRetry() {
        trySearch()
    }
}