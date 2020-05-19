package ku.olga.route_builder.presentation.search.category

import android.content.SharedPreferences
import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.BoundingBox
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.POI
import ku.olga.route_builder.domain.repository.POIRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import org.osmdroid.util.GeoPoint
import java.lang.Exception

class CategoryPresenter(private val poiRepository: POIRepository,
                        private val preferences: SharedPreferences) : BasePresenter<CategoryView>() {
    var category: Category? = null
    private val pois = mutableListOf<POI>()
    private var job: Job? = null
    private var boundingBox: BoundingBox? = null

    override fun attachView(view: CategoryView) {
        super.attachView(view)
        view.showPOIs(pois)
        view.moveTo(getCenter(), false)
    }

    fun onBoundingBoxChanged(latitude: Double?, longitude: Double?, boundingBox: BoundingBox) {
        this.boundingBox = boundingBox
        setCenter(latitude, longitude)
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
        withContext(Dispatchers.Main) { view?.showPOIs(pois) }
    }

    fun getTitle(): String = category?.title ?: ""

    private fun getCenter() = GeoPoint(preferences.getDouble(LATITUDE, DEFAULT_LATITUDE),
            preferences.getDouble(LONGITUDE, DEFAULT_LONGITUDE))

    private fun setCenter(latitude: Double?, longitude: Double?) {
        if (latitude != null && longitude != null)
            preferences.edit().putDouble(LATITUDE, latitude).putDouble(LONGITUDE, longitude).apply()
    }

    private fun SharedPreferences.Editor.putDouble(key: String, double: Double): SharedPreferences.Editor =
            putLong(key, java.lang.Double.doubleToRawLongBits(double))

    private fun SharedPreferences.getDouble(key: String, defaultDouble: Double): Double =
            java.lang.Double.longBitsToDouble(getLong(key,
                    java.lang.Double.doubleToRawLongBits(defaultDouble)))

    companion object {
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val DEFAULT_LATITUDE = 54.180857
        private const val DEFAULT_LONGITUDE = 45.186319
    }
}