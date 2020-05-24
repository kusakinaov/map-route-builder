package ku.olga.route_builder.presentation.search.category

import ku.olga.route_builder.domain.model.POI
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseLocationView
import ku.olga.route_builder.presentation.base.BaseView

interface CategoryView : BaseLocationView {
    fun onResume()
    fun onPause()
    fun setPOIs(pois: List<POI>)
    fun hidePOIDetails(): Boolean
    fun moveTo(latitude: Double, longitude: Double, animate: Boolean)
    fun moveTo(pois: List<POI>, animate: Boolean)
    fun openEditPOI(userPoint: UserPoint)
}