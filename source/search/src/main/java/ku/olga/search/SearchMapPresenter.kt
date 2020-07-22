package ku.olga.search

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlinx.coroutines.*
import ku.olga.core_api.dto.*
import ku.olga.core_api.repository.AddressRepository
import ku.olga.core_api.repository.POIRepository
import ku.olga.ui_core.base.BaseLocationPresenter
import ku.olga.ui_core.view.MAX_ZOOM_LEVEL
import javax.inject.Inject

class SearchMapPresenter @Inject constructor(private val poiRepository: POIRepository,
                                             private val addressRepository: AddressRepository,
                                             preferences: SharedPreferences) : BaseLocationPresenter<SearchMapView>(preferences) {
    private var boundingBox: BoundingBox? = null
    private var category: Category? = null

    private var query: String? = null
    private val hasQuery: Boolean
        get() = query?.length ?: 0 > 2
    private val queryHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == WHAT_QUERY) {
                query = msg.obj as String
                setupState()
            }
        }
    }

    private var state: State = State.CATEGORIES

    private val addresses = mutableListOf<SearchAddress>()
    private val categories = mutableListOf<Category>()
    private val pois = mutableListOf<POI>()

    private var addressesJob: Job? = null

    override fun attachView(view: SearchMapView) {
        super.attachView(view)

        view.bindQuery(query)
        view.bindClearButton(state == State.POIS || state == State.ADDRESSES)

        boundingBox?.let { view.bindBoundingBox(it) }
        bindState()
    }

    fun onQueryChanged(query: String?) {
        if (this.query != query) {
            queryHandler.removeMessages(WHAT_QUERY)
            queryHandler.sendMessageDelayed(queryHandler.obtainMessage(WHAT_QUERY, query), DELAY_QUERY)
        }
    }

    fun onPickCategory(category: Category?) {
        if (this.category != category) {
            this.category = category
            this.query = ""

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

    fun onClickClear() {
        if (category != null) {
            onPickCategory(null)
        } else if (hasQuery) {
            view?.bindQuery("")
            onQueryChanged("")
        }
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
            State.ADDRESSES -> setAddressesState()
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
        view?.bindClearButton(false)

        if (categories.isEmpty()) loadCategories()
    }

    private fun searchAddresses() = CoroutineScope(Dispatchers.IO).launch {
        try {
            setAddresses(addressRepository.searchAddress(query))
        } catch (e: Exception) {
            e.printStackTrace()
            view?.showDefaultError()
        }
        withContext(Dispatchers.Main) { bindAddresses() }
    }

    private fun setAddressesState() {
        setPOIs(emptyList())

        view?.showAddresses()
        bindAddresses()
        view?.bindClearButton(true)

        addressesJob?.cancel()
        if (hasQuery) {
            addressesJob = searchAddresses()
        }
    }

    private fun setAddresses(addresses: List<SearchAddress>) {
        this.addresses.clear()
        this.addresses.addAll(addresses)
    }

    private fun bindAddresses() {
        view?.bindAddresses(addresses)
    }

    private fun setPOIsState() {
        setAddresses(emptyList())

        view?.showPOIs(category)
        bindPOIs()
        view?.bindClearButton(true)

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

    fun bindQuery() {
        view?.bindQuery(query)
    }

    enum class State {
        CATEGORIES, ADDRESSES, POIS
    }

    companion object {
        private const val WHAT_QUERY = 1001
        private const val DELAY_QUERY = 1000L
    }
}