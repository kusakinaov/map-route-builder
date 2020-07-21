package ku.olga.search

import android.content.SharedPreferences
import ku.olga.core_api.dto.BoundingBox
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.Coordinates
import ku.olga.ui_core.base.BaseLocationPresenter
import ku.olga.ui_core.view.MAX_ZOOM_LEVEL
import javax.inject.Inject

class SearchMapPresenter @Inject constructor(preferences: SharedPreferences) :
        BaseLocationPresenter<SearchMapView>(preferences) {
    private var query: String? = null
    private var category: Category? = null
    private var boundingBox: BoundingBox? = null
    private val hasQuery: Boolean
        get() = query?.length ?: 0 > 2
    private var state: State = State.CATEGORIES

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
        }
    }

    fun onPickCategory(category: Category?) {
        if (this.category != category) {
            this.category = category
            this.query = ""

            setupState()
            view?.bindCategory(category)
        }
    }

    fun onBoundingBoxChanged(
            latitude: Double,
            longitude: Double,
            boundingBox: BoundingBox,
            zoomLevel: Double
    ) {
        this.boundingBox = boundingBox
    }

    override fun onCoordinatesChanged(coordinates: Coordinates) {
        super.onCoordinatesChanged(coordinates)
        view?.moveTo(coordinates.latitude, coordinates.longitude, MAX_ZOOM_LEVEL, true)
    }

    fun onClickClearCategory() {
        category = null
        view?.bindCategory(category)
    }

    private fun setupState() {
        state = when {
            category != null -> State.POIS
            hasQuery -> State.ADDRESSES
            else -> State.CATEGORIES
        }
    }

    private fun bindState() {
        when (state) {
            State.POIS -> {
            }
            State.ADDRESSES -> {
            }
            else -> {
            }
        }
    }

    enum class State {
        CATEGORIES, ADDRESSES, POIS
    }
}