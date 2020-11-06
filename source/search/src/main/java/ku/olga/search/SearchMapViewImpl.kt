package ku.olga.search

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_search_map.view.*
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.POI
import ku.olga.core_api.dto.SearchAddress
import ku.olga.ui_core.utils.hasLocationPermission
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

abstract class SearchMapViewImpl(
    private val view: View,
    private val presenter: SearchMapPresenter
) : SearchMapView {
    override var searchView: SearchView? = null
        set(value) {
            field = value
            value?.setOnQueryTextListener(buildQueryTextListener())
            presenter.bindQuery()
        }

    private lateinit var markerOverlay: RadiusMarkerClusterer

    private val categoriesAdapter = CategoriesAdapter().apply {
        categoryClickListener = { presenter.onPickCategory(it) }
    }
    private val addressesAdapter = AddressesAdapter().apply {
        onClickAddressListener = { presenter.onClickAddress(it) }
    }
    private val poisAdapter = POIAdapter().apply {
        onClickListener = { presenter.onClickPOI(it) }
    }

    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    override fun onAttach() {
        setupMap()
        view.apply {
            setupBottomSheet(layoutBottomSheet)

            imageViewClear.setOnClickListener { presenter.onClickClear() }
            recyclerItems.apply {
                layoutManager = LinearLayoutManager(context)
            }
            layoutHeader.apply {
                addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, _ ->
                    bottomSheetBehavior?.peekHeight = bottom
                }
                setOnClickListener { toggleBottomSheetState() }
            }
        }
        presenter.attachView(this)
    }

    private fun setupMap() {
        view.mapView?.let {
            markerOverlay = buildRadiusMarkerClusterer(
                it.context,
                ContextCompat.getDrawable(it.context, R.drawable.cluster)!!,
                ContextCompat.getColor(it.context, R.color.map_icon_text)
            )
            initMapView(it, buildMapListener(), listOf(markerOverlay))
            it.controller.setZoom(MIN_ZOOM_LEVEL)
        }
    }

    private fun setupBottomSheet(layoutBottomSheet: ConstraintLayout) {
        bottomSheetBehavior =
            BottomSheetBehavior.from(layoutBottomSheet).apply {
                isHideable = false
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (slideOffset == 0f || slideOffset == 1f) {
                            hideKeyboard()
                        }
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                    }
                })
            }
    }

    abstract fun hideKeyboard()

    override fun onDetach() {
        presenter.detachView()
    }

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun bindBoundingBox(boundingBox: AppBoundingBox) {
        view.mapView?.let {
            ku.olga.ui_core.view.moveTo(
                it, listOf(
                    GeoPoint(boundingBox.latSouth, boundingBox.lonEast),
                    GeoPoint(boundingBox.latNorth, boundingBox.lonWest)
                ), false
            )
        }
    }

    override fun bindCategories(categories: List<Category>) {
        categoriesAdapter.setItems(categories)
        view.mapView?.let {
            markerOverlay.items.clear()
            markerOverlay.invalidate()
            it.invalidate()
        }
    }

    override fun bindAddresses(addresses: List<SearchAddress>) {
        addressesAdapter.setItems(addresses)
        view.mapView?.let {
            val icon = ContextCompat.getDrawable(it.context, R.drawable.ic_place)
            markerOverlay.items.clear()
            for (address in addresses) {
                markerOverlay.add(
                    buildMarker(
                        address.postalAddress,
                        address.lat,
                        address.lon,
                        icon,
                        Marker.OnMarkerClickListener { _, _ ->
                            presenter.onClickAddress(address)
                            true
                        })
                )
            }
            markerOverlay.invalidate()
            it.invalidate()
        }
    }

    override fun bindPOIs(pois: List<POI>) {
        poisAdapter.setItems(pois)
        view.mapView?.let {
            val icon = ContextCompat.getDrawable(it.context, R.drawable.ic_place)
            markerOverlay.items.clear()
            for (poi in pois) {
                markerOverlay.add(
                    buildMarker(
                        poi.title,
                        poi.latitude,
                        poi.longitude,
                        icon,
                        Marker.OnMarkerClickListener { _, _ ->
                            presenter.onClickPOI(poi)
                            true
                        })
                )
            }
            markerOverlay.invalidate()
            it.invalidate()
        }
    }

    private fun buildMarker(
        title: String,
        latitude: Double,
        longitude: Double,
        poiIcon: Drawable?,
        markerClickListener: Marker.OnMarkerClickListener? = null
    ): Marker =
        Marker(view.mapView).apply {
            this.title = title
            position = GeoPoint(latitude, longitude)
            icon = poiIcon
            setOnMarkerClickListener(markerClickListener)
        }

    override fun hideAll() {
        markerOverlay.items.clear()
        markerOverlay.invalidate()
        view.mapView?.invalidate()
    }

    override fun showPOIs(category: Category?) {
        view.apply {
            textViewTitle.text = category?.title ?: ""
            recyclerItems.adapter = poisAdapter
        }
    }

    override fun showAddresses() {
        view.apply {
            textViewTitle.setText(R.string.ttl_search_results)
            recyclerItems.adapter = addressesAdapter
        }
    }

    override fun showCategories() {
        view.apply {
            textViewTitle.setText(R.string.ttl_categories)
            recyclerItems.adapter = categoriesAdapter
        }
    }

    override fun bindClearButton(visible: Boolean) {
        view.imageViewClear?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun hasLocationPermission(): Boolean = hasLocationPermission(view.context)

    override fun onResume() {
        view.mapView?.onResume()
    }

    override fun onPause() {
        view.mapView?.onPause()
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        view.mapView?.let {
            ku.olga.ui_core.view.moveTo(it, latitude, longitude, zoomLevel, animated)
        }
    }

    override fun moveTo(geoPoints: List<GeoPoint>, animated: Boolean) {
        view.mapView?.let {
            ku.olga.ui_core.view.moveTo(it, geoPoints, animated)
        }
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

    private fun toggleBottomSheetState() {
        if (isBottomSheetExpanded) {
            closeBottomSheet()
        } else {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun closeBottomSheet(): Boolean {
        val expanded = isBottomSheetExpanded
        hideKeyboard()
        if (expanded) bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        return expanded
    }

    private val isBottomSheetExpanded: Boolean
        get() = bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED

    companion object {
        private const val DELAY_LOAD_POI = 1000L
    }
}