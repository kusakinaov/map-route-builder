package ku.olga.route_builder.presentation.search.category

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.BoundingBox
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.POI
import ku.olga.route_builder.domain.repository.POIRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import java.lang.Exception

class CategoryPresenter(private val poiRepository: POIRepository) : BasePresenter<CategoryView>() {
    var category: Category? = null
    private val pois = mutableListOf<POI>()
    private var job: Job? = null
    private var boundingBox: BoundingBox? = null

    override fun attachView(view: CategoryView) {
        super.attachView(view)

        view.showPOIs(pois)
    }

    fun onBoundingBoxChanged(boundingBox: BoundingBox) {
        this.boundingBox = boundingBox
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
            view?.showDefaultError()
        }
        withContext(Dispatchers.Main) { view?.showPOIs(pois) }
    }

    fun getTitle(): String = category?.title ?: ""
}