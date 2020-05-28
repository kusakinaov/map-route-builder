package ku.olga.ui_core

interface BaseLocationView : BaseView {
    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
}