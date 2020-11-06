package ku.olga.ui_core.view

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import ku.olga.ui_core.utils.convertDpToPx
import ku.olga.ui_core.utils.convertSpToPx
import ku.olga.ui_core.utils.getBitmap
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline

const val MIN_ZOOM_LEVEL = 3.0
const val MAX_ZOOM_LEVEL = 20.0
const val NONE_MOVE_SPEED = 0L
const val DEFAULT_MOVE_SPEED = 500L
const val BORDER_SIZE = 40f

fun buildRadiusMarkerClusterer(context: Context, drawable: Drawable, textColor: Int) =
    RadiusMarkerClusterer(context).apply {
        setIcon(getBitmap(drawable))
        textPaint.apply {
            color = textColor
            textSize =
                convertSpToPx(context.resources, 16f)
        }
    }

fun buildDirectionsPolyline(context: Context, color: Int) = Polyline().apply {
    outlinePaint.apply {
        isAntiAlias = true
        this.color = color
        strokeWidth = convertDpToPx(context.resources, 4f)
        style = Paint.Style.STROKE
    }
    isGeodesic = true
}

fun initMapView(mapView: MapView, mapListener: MapListener, overlays: List<Overlay>) {
    mapView.apply {
        setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        zoomController.apply {
            setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            minZoomLevel = MIN_ZOOM_LEVEL
            maxZoomLevel = MAX_ZOOM_LEVEL
        }
        setMultiTouchControls(true)
        addMapListener(mapListener)
        this.overlays.addAll(overlays)
    }
}

fun moveTo(
    mapView: MapView,
    latitude: Double,
    longitude: Double,
    zoomLevel: Double,
    animate: Boolean
) {
    mapView.controller?.apply {
        animateTo(
            GeoPoint(latitude, longitude),
            zoomLevel, if (animate) DEFAULT_MOVE_SPEED else NONE_MOVE_SPEED
        )
    }
}

fun moveTo(mapView: MapView, geoPoints: List<GeoPoint>, animated: Boolean) {
    val boundingBox = buildBoundingBox(geoPoints)
    mapView.post {
        mapView.zoomToBoundingBox(
            boundingBox, animated, convertDpToPx(
                mapView.resources,
                BORDER_SIZE
            ).toInt()
        )
    }
}

private fun buildBoundingBox(geoPoints: List<GeoPoint>) = BoundingBox.fromGeoPointsSafe(geoPoints)