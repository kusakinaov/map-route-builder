package ku.olga.route_builder.presentation.search.category

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.BoundingBox
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.Coordinates
import ku.olga.route_builder.domain.model.POI
import ku.olga.route_builder.domain.repository.POIRepository
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BasePresenter
import java.lang.Exception

class CategoryPresenter(private val poiRepository: POIRepository) : BasePresenter<CategoryView>() {
    var category: Category? = null
    private val pois = mutableListOf<POI>()
    private var job: Job? = null
    private var boundingBox: BoundingBox? = null

    override fun attachView(view: CategoryView) {
        super.attachView(view)
        if (pois.isEmpty()) {
            App.application.getLastCoordinates().let {
                view.moveTo(it.latitude, it.longitude, false)
            }
        } else bindPOIs()
    }

    fun onBoundingBoxChanged(latitude: Double, longitude: Double, boundingBox: BoundingBox) {
        this.boundingBox = boundingBox
        App.application.setLastCoordinates(Coordinates(latitude, longitude))
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
        withContext(Dispatchers.Main) { bindPOIs() }
    }

    private fun bindPOIs() {
        view?.apply {
            setPOIs(pois)
            if (pois.size == 1) {
                val poi = pois[0]
                moveTo(poi.latitude, poi.longitude, true)
            }
        }
    }

    fun getTitle(): String = category?.title ?: ""
}