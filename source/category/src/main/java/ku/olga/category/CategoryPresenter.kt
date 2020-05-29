package ku.olga.category

import android.content.SharedPreferences
import kotlinx.coroutines.*
import ku.olga.core_api.dto.*
import ku.olga.core_api.repository.POIRepository
import ku.olga.ui_core.base.BaseLocationPresenter
import ku.olga.ui_core.utils.getLastCoordinates
import java.lang.Exception
import javax.inject.Inject

class CategoryPresenter @Inject constructor(
    private val poiRepository: POIRepository,
    preferences: SharedPreferences
) : BaseLocationPresenter<CategoryView>(preferences) {
    var category: Category? = null
    private val pois = mutableListOf<POI>()
    private var job: Job? = null
    private var boundingBox: BoundingBox? = null
    private var center: Coordinates? = null
    private var zoomLevel =
        DEFAULT_ZOOM_LEVEL

    override fun attachView(view: CategoryView) {
        super.attachView(view)
        when {
            center != null -> moveToCenter()
            pois.isEmpty() -> moveToMyCoordinates()
            pois.isNotEmpty() -> bindPOIs(false)
        }
    }


    private fun moveToCenter() {
        center?.let {
            view?.moveTo(it.latitude, it.longitude, zoomLevel, false)
        }
    }

    private fun moveToMyCoordinates() {
        getLastCoordinates(preferences).let {
            view?.moveTo(it.latitude, it.longitude, zoomLevel, false)
        }
    }

    fun onBoundingBoxChanged(
        latitude: Double,
        longitude: Double,
        boundingBox: BoundingBox,
        zoomLevel: Double
    ) {
        this.boundingBox = boundingBox
        center = Coordinates(latitude, longitude)
        this.zoomLevel = zoomLevel
        if (job?.isActive == true) job?.cancel()
        job = loadPOIs(boundingBox)
    }

    private fun loadPOIs(boundingBox: BoundingBox): Job = CoroutineScope(Dispatchers.IO).launch {
        try {
            category?.let {
                pois.clear()
                pois.addAll(poiRepository.getPOIs(boundingBox, it))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
        withContext(Dispatchers.Main) { bindPOIs(false) }
    }

    private fun bindPOIs(move: Boolean, animated: Boolean = true) {
        view?.apply {
            setPOIs(pois)
            if (move) {
                when {
                    pois.size == 1 -> pois[0].let {
                        moveTo(
                            it.latitude,
                            it.longitude,
                            zoomLevel,
                            true
                        )
                    }
                    pois.isNotEmpty() -> moveTo(pois, animated)
                }
            }
        }
    }

    fun getTitle(): String = category?.title ?: ""

    fun onClickAddPOI(poi: POI) {
        view?.apply {
            hidePOIDetails()
            openEditPOI(
                UserPoint(
                    null, poi.title, poi.description,
                    poi.latitude, poi.longitude, null, UserPointType.POI
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15.0
    }
}