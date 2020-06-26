package ku.olga.core_impl.repository

import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import ku.olga.core_api.dto.Coordinates
import ku.olga.core_api.repository.DirectionsRepository
import javax.inject.Inject

class GoogleDirectionsRepository @Inject constructor(private val geoApiContext: GeoApiContext) :
    DirectionsRepository {
    override suspend fun getDirections(points: List<Coordinates>): List<Coordinates> {
        if (points.size > 1) {
            val waypoints: Array<LatLng> = if (points.size > 2) {
                points.subList(1, points.size - 1).map { it.toLatLng() }.toTypedArray()
            } else emptyArray()

            val directionsResult = DirectionsApi.newRequest(geoApiContext)
                .origin(points[0].toLatLng())
                .destination(points.last().toLatLng()).waypoints(*waypoints).await()
            return directionsResult.routes[0]
                .overviewPolyline?.decodePath()?.map { Coordinates(it.lat, it.lng) } ?: emptyList()
        }
        return emptyList()
    }

    private fun Coordinates.toLatLng() = LatLng(latitude, longitude)
}