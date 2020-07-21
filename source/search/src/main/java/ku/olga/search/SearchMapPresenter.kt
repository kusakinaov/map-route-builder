package ku.olga.search

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.olga.core_api.dto.*
import ku.olga.core_api.repository.POIRepository
import ku.olga.ui_core.base.BaseLocationPresenter
import ku.olga.ui_core.view.MAX_ZOOM_LEVEL
import javax.inject.Inject

class SearchMapPresenter @Inject constructor(
    private val poiRepository: POIRepository,
    preferences: SharedPreferences
) :
    BaseLocationPresenter<SearchMapView>(preferences) {
    private var boundingBox: BoundingBox? = null
    private var category: Category? = null
    private var query: String? = null
    private val hasQuery: Boolean
        get() = query?.length ?: 0 > 2

    private var state: State = State.CATEGORIES

    private val addresses = mutableListOf<SearchAddress>()
    private val categories = mutableListOf<Category>()
    private val pois = mutableListOf<POI>()

    override fun attachView(view: SearchMapView) {
        super.attachView(view)

        view.bindCategory(category)
        view.bindQuery(query)

        boundingBox?.let { view.bindBoundingBox(it) }
        bindState()
    }

    fun onQueryChanged(query: String?) {
        if (this.query != query) {
            this.query = query

            setupState()
        }
    }

    fun onPickCategory(category: Category?) {
        if (this.category != category) {
            this.category = category
            this.query = ""

            view?.bindCategory(category)
            setupState()
        }
    }

    fun onBoundingBoxChanged(
        latitude: Double,
        longitude: Double,
        boundingBox: BoundingBox,
        zoomLevel: Double
    ) {
        this.boundingBox = boundingBox
        bindState()
    }

    override fun onCoordinatesChanged(coordinates: Coordinates) {
        super.onCoordinatesChanged(coordinates)
        view?.moveTo(coordinates.latitude, coordinates.longitude, MAX_ZOOM_LEVEL, true)
    }

    fun onClickClearCategory() {
        onPickCategory(null)
    }

    private fun setupState() {
        state = when {
            category != null -> State.POIS
            hasQuery -> State.ADDRESSES
            else -> State.CATEGORIES
        }
        bindState()
    }

    private fun bindState() {
        when (state) {
            State.POIS -> setPOIsState()
            State.ADDRESSES -> {
                view?.showAddresses()
                view?.bindAddresses(addresses)
            }
            State.CATEGORIES -> setCategoriesState()
        }
    }

    private fun loadCategories() = CoroutineScope(Dispatchers.IO).launch {
        try {
            setCategories(poiRepository.getCategories(null))
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
        withContext(Dispatchers.Main) { bindCategories() }
    }

    private fun setCategories(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
    }

    private fun bindCategories() {
        view?.bindCategories(categories)
    }

    private fun setCategoriesState() {
        view?.showCategories()
        bindCategories()
        if (categories.isEmpty()) loadCategories()
    }

    private fun setAddresses(addresses: List<SearchAddress>) {
        this.addresses.clear()
        this.addresses.addAll(addresses)
    }

    private fun bindAddresses() {
        view?.bindAddresses(addresses)
    }

    private fun setPOIsState() {
        view?.showPOIs()
        view?.bindPOIs(pois)
        if (category != null && boundingBox != null) {
            loadPOIs(boundingBox!!, category!!)
        }
    }

    private fun loadPOIs(boundingBox: BoundingBox, category: Category) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                setPOIs(poiRepository.getPOIs(query, boundingBox, category))
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { view?.showDefaultError() }
            }
            withContext(Dispatchers.Main) { bindPOIs() }
        }

    private fun setPOIs(pois: List<POI>) {
        this.pois.clear()
        this.pois.addAll(pois)
    }

    private fun bindPOIs() {
        view?.bindPOIs(pois)
    }

    enum class State {
        CATEGORIES, ADDRESSES, POIS
    }
}