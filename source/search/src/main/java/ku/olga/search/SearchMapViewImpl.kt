package ku.olga.search

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search_map.view.*
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.POI
import ku.olga.core_api.dto.SearchAddress
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import ku.olga.ui_core.view.MIN_ZOOM_LEVEL
import ku.olga.ui_core.view.buildRadiusMarkerClusterer
import ku.olga.ui_core.view.initMapView
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ku.olga.core_api.dto.BoundingBox as AppBoundingBox

class SearchMapViewImpl(
        private val fragment: Fragment,
        private val presenter: SearchMapPresenter
) : SearchMapView {
    override var searchView: SearchView? = null
    override var mapView: MapView? = null

    private lateinit var markerOverlay: RadiusMarkerClusterer

    private val categoriesAdapter = CategoriesAdapter()
    private val addressesAdapter = AddressesAdapter()
    private val poisAdapter = POIAdapter()

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun bindCategory(category: Category?) {
        val hasCategory = category != null
        fragment.view?.apply {
            textViewTitle.text = category?.title ?: context.getString(R.string.ttl_categories)
            imageViewClear.visibility = if (hasCategory) View.VISIBLE else View.GONE
            recyclerCategories.visibility = if (hasCategory) View.VISIBLE else View.GONE
            recyclerItems.visibility = if (hasCategory) View.GONE else View.VISIBLE
        }
    }

    override fun bindBoundingBox(boundingBox: AppBoundingBox) {
        mapView?.let {
            ku.olga.ui_core.view.moveTo(
                    it, listOf(
                    GeoPoint(boundingBox.latSouth, boundingBox.lonEast),
                    GeoPoint(boundingBox.latNorth, boundingBox.lonWest)
            ), false
            )
        }
    }

    override fun showCategories(categories: List<Category>) {
        categoriesAdapter.setItems(categories)
        bindRecyclerViews(categoriesVisible = true)
        mapView?.let {
            markerOverlay.items.clear()
            markerOverlay.invalidate()
        }
    }

    override fun showAddresses(addresses: List<SearchAddress>) {
        addressesAdapter.setItems(addresses)
        bindRecyclerViews(addressesVisible = true)
        mapView?.let {
            val icon = ContextCompat.getDrawable(it.context, R.drawable.ic_place)
            markerOverlay.items.clear()
            for (address in addresses) {
                markerOverlay.add(buildMarker(address.postalAddress, address.lat, address.lon, icon))
            }
            markerOverlay.invalidate()
        }
    }

    override fun showPOIs(pois: List<POI>) {
        poisAdapter.setItems(pois)
        bindRecyclerViews(poisVisible = true)
        mapView?.let {
            val icon = ContextCompat.getDrawable(it.context, R.drawable.ic_place)
            markerOverlay.items.clear()
            for (poi in pois) {
                markerOverlay.add(buildMarker(poi.title, poi.latitude, poi.longitude, icon))
            }
            markerOverlay.invalidate()
        }
    }

    private fun buildMarker(title: String, latitude: Double, longitude: Double, poiIcon: Drawable?): Marker =
            Marker(mapView).apply {
                this.title = title
                position = GeoPoint(latitude, longitude)
                icon = poiIcon
                setOnMarkerClickListener { _, _ -> true }
            }

    private fun bindRecyclerViews(addressesVisible: Boolean = false, categoriesVisible: Boolean = false, poisVisible: Boolean = false) {
        fragment.view?.apply {
            recyclerAddresses.visibility = if (addressesVisible) View.VISIBLE else View.GONE
            recyclerCategories.visibility = if (categoriesVisible) View.VISIBLE else View.GONE
            recyclerItems.visibility = if (poisVisible) View.VISIBLE else View.GONE
        }
    }

    override fun hideAll() {
        markerOverlay.items.clear()
        markerOverlay.invalidate()
    }

    override fun hasLocationPermission(): Boolean =
            ku.olga.ui_core.utils.hasLocationPermission(fragment.context)

    override fun requestLocationPermission() {
        ku.olga.ui_core.utils.requestLocationPermission(fragment, REQ_CODE_LOCATION_PERMISSION)
    }

    override fun onResume() {
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        mapView?.let {
            ku.olga.ui_core.view.moveTo(it, latitude, longitude, zoomLevel, animated)
        }
    }

    override fun moveTo(geoPoints: List<GeoPoint>, animated: Boolean) {
        mapView?.let {
            ku.olga.ui_core.view.moveTo(it, geoPoints, animated)
        }
    }

    override fun onAttach() {
        mapView?.let {
            markerOverlay = buildRadiusMarkerClusterer(
                    it.context,
                    ContextCompat.getDrawable(it.context, R.drawable.cluster)!!,
                    ContextCompat.getColor(it.context, R.color.map_icon_text)
            )
            initMapView(it, buildMapListener(), listOf(markerOverlay))
            it.controller.setZoom(MIN_ZOOM_LEVEL)
        }
        fragment.view?.apply {
            imageViewClear.setOnClickListener { presenter.onClickClearCategory() }
            recyclerCategories.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = categoriesAdapter
            }
            recyclerAddresses.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addressesAdapter
            }
            recyclerItems.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = poisAdapter
            }
        }
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }

    private fun buildMapListener() = DelayedMapListener(object : MapListener {
        override fun onScroll(event: ScrollEvent?): Boolean {
            event?.source?.let { onMapChanged(it) }
            return true
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.source?.let { onMapChanged(it) }
            return true
        }
    }, DELAY_LOAD_POI)

    private fun onMapChanged(mapView: MapView) {
        mapView.mapCenter.let {
            presenter.onBoundingBoxChanged(
                    it.latitude, it.longitude,
                    mapView.boundingBox.toAppBoundingBox(), mapView.zoomLevelDouble
            )
        }
        searchView?.setOnQueryTextListener(buildQueryTextListener())
    }

    private fun buildQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            presenter.onQueryChanged(newText)
            return true
        }
    }

    private fun BoundingBox.toAppBoundingBox() =
            AppBoundingBox(latNorth, lonEast, latSouth, lonWest)

    companion object {
        private const val DELAY_LOAD_POI = 1000L
    }
}