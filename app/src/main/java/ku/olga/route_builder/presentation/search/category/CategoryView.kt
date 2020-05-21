package ku.olga.route_builder.presentation.search.category

import ku.olga.route_builder.domain.model.POI
import ku.olga.route_builder.presentation.base.BaseView

interface CategoryView : BaseView {
    fun onResume()
    fun onPause()
    fun setPOIs(pois: List<POI>)
    fun hidePOIDetails(): Boolean
    fun moveTo(latitude: Double, longitude: Double, animate: Boolean)
}