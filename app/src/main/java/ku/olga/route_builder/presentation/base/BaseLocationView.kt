package ku.olga.route_builder.presentation.base

interface BaseLocationView : BaseView {
    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
}