package ku.olga.category

import ku.olga.core_api.dto.POI
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseLocationView

interface CategoryView : BaseLocationView {
    fun onResume()
    fun onPause()
    fun setPOIs(pois: List<POI>)
    fun hidePOIDetails(): Boolean
    fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animate: Boolean)
    fun moveTo(pois: List<POI>, animate: Boolean)
    fun openEditPOI(userPoint: UserPoint)
}