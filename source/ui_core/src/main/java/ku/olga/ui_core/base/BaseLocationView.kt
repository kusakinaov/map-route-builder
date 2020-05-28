package ku.olga.ui_core.base

interface BaseLocationView : BaseView {
    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
}