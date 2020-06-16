package ku.olga.route_builder.data.repository

import com.google.gson.GsonBuilder
import ku.olga.route_builder.domain.model.Coordinates
import ku.olga.route_builder.domain.repository.DirectionsRepository
import java.net.URL
import javax.inject.Inject

class OSRMDirectionsRepository @Inject constructor() : DirectionsRepository {
    private val gson = GsonBuilder().create()

    override suspend fun getDirections(points: List<Coordinates>): List<Coordinates> {
        val text = URL(buildServerUrl(SERVER, Service.trip, Profile.foot, VERSION, points)).readText()

        return emptyList()
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
            transform = { "${it.latitude},${it.longitude}" })}"

    companion object {
        private const val SERVER = "http://router.project-osrm.org"
        private const val VERSION = "v1"
    }

    enum class Profile { car, bike, foot }
    enum class Service { route, nearest, table, match, trip, tile }
}