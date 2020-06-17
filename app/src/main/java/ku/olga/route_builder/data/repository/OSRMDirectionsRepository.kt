package ku.olga.route_builder.data.repository

import com.google.gson.GsonBuilder
import ku.olga.route_builder.data.repository.osrm.TripResponse
import ku.olga.route_builder.domain.model.Coordinates
import ku.olga.route_builder.domain.repository.DirectionsRepository
import java.net.URL
import javax.inject.Inject

class OSRMDirectionsRepository @Inject constructor() : DirectionsRepository {
    private val gson = GsonBuilder().create()

    override suspend fun getDirections(points: List<Coordinates>): List<Coordinates> {
        val url = buildServerUrl(SERVER, Service.trip, Profile.foot, VERSION, points)
        val text = URL(url).readText()
        val coordinates = mutableListOf<Coordinates>()
        gson.fromJson(text, TripResponse::class.java).trips.let {
            if (it.isNotEmpty()) {
                for (leg in it[0].legs) {
                    for (step in leg.steps) {
                        for (intersection in step.intersections) {
                            intersection.location.let {
                                coordinates.add(Coordinates(it[1], it[0]))
                            }
                        }
                    }
                }
            }
        }
        return coordinates
    }

    private fun buildServerUrl(
        server: String,
        service: Service,
        profile: Profile,
        version: String,
        coordinates: List<Coordinates>
    ) =
        "$server/${service.name}/$version/${profile.name}/${coordinates.joinToString(
            separator = ";",
            transform = { "${it.longitude},${it.latitude}" })}?steps=true"//&geometries=geojson"

    companion object {
        private const val SERVER = "http://router.project-osrm.org"
        private const val VERSION = "v1"
    }

    enum class Profile { car, bike, foot }
    enum class Service { route, nearest, table, match, trip, tile }
}