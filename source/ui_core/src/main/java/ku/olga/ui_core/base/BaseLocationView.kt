package ku.olga.ui_core.base

import ku.olga.core_api.dto.Coordinates

interface BaseLocationView : BaseView {
    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
    fun onCoordinatesChanged(coordinates: Coordinates) {}
}