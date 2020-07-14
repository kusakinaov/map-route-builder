package ku.olga.ui_core.base

import org.osmdroid.util.GeoPoint

interface BaseMapView : BaseView {
    fun onResume()
    fun onPause()
    fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean)
    fun moveTo(geoPoints: List<GeoPoint>, animated: Boolean)
}