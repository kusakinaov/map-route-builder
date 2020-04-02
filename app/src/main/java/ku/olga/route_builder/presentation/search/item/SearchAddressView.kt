package ku.olga.route_builder.presentation.search.item

import android.os.Bundle
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BaseView

interface SearchAddressView : BaseView {
    fun onCreate(state: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun bindLatLng(lat: Double, lon: Double)
    fun bindAddress(postalAddress: String)
    fun editPoint(postalAddress: String, lat: Double, lon: Double)
}