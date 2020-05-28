package ku.olga.search

import android.content.SharedPreferences
import kotlinx.coroutines.*
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress
import ku.olga.core_api.repository.AddressRepository
import ku.olga.core_api.repository.POIRepository
import ku.olga.ui_core.base.BaseLocationPresenter
import java.io.IOException
import javax.inject.Inject

class SearchAddressesPresenter @Inject constructor(
    private val addressRepository: AddressRepository,
    private val poiRepository: POIRepository,
    preferences: SharedPreferences
) : BaseLocationPresenter<ku.olga.search.SearchAddressesView>(preferences) {
    private var query: String? = null
    private var job: Job? = null
    private val addresses = mutableListOf<SearchAddress>()
    private val categories = mutableListOf<Category>()

    override fun attachView(view: ku.olga.search.SearchAddressesView) {
        super.attachView(view)
        bindQuery()
        bindData()

        if (!isValidQuery() && categories.isEmpty()) loadCategories()
    }

    fun onQueryChanged(query: String?) {
        view?.let {
            this.query = query
            trySearch()
        }
    }

    private fun runSearch() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val addresses = addressRepository.searchAddress(query)
            withContext(Dispatchers.Main) { setAddresses(addresses) }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
    }

    private fun loadCategories() = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Dispatchers.Main) { showProgress() }
            val categories = poiRepository.getCategories(query)
            withContext(Dispatchers.Main) { setCategories(categories) }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
    }

    private fun setCategories(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        bindData()
    }

    private fun setAddresses(addresses: List<SearchAddress>?) {
        this.addresses.clear()
        if (addresses?.isNotEmpty() == true) {
            this.addresses.addAll(addresses)
        }
        bindData()
    }

    private fun bindData() {
        view?.apply {
            bindAddresses(addresses)
            bindCategories(categories)
            if (isValidQuery()) {
                if (addresses.isEmpty()) {
                    showEmpty()
                } else {
                    showAddresses()
                }
            } else {
                showCategories()
            }
        }
    }

    fun bindQuery() {
        view?.bindQuery(query)
    }

    fun onClickRetry() {
        trySearch()
    }

    private fun isValidQuery() = query?.length ?: 0 >= 3

    private fun trySearch() {
        if (isValidQuery()) {
            job?.let { if (it.isActive) it.cancel() }
            job = runSearch()
        } else {
            addresses.clear()
            bindData()
        }
    }
}