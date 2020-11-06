package ku.olga.osrm

import com.google.gson.GsonBuilder
import ku.olga.osrm.gson.LocationTypeAdapter
import ku.olga.osrm.model.Coordinates
import ku.olga.osrm.model.Profile
import ku.olga.osrm.model.Service
import ku.olga.osrm.model.TripResponse
import java.net.URL

object DirectionsService {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Coordinates::class.java, LocationTypeAdapter()).create()

    fun getTrip(
        points: List<Coordinates>,
        profile: Profile,
        service: Service = Service.trip
    ): TripResponse {
        val url = buildServerUrl(OSRM_SERVER, service, profile, OSRM_VERSION, points)
        val text = URL(url).readText()
        return gson.fromJson(text, TripResponse::class.java)
    }

    private fun buildServerUrl(
        server: String,
        service: Service,
        profile: Profile,
        version: String,
        coordinates: List<Coordinates>
    ) =
        "$server/${service.name}/$version/${profile.name}/${
            coordinates.joinToString(
                separator = ";",
                transform = { "${it.longitude},${it.latitude}" })
        }?steps=true"//&geometries=geojson"
}