package ku.olga.route_builder.data.repository.osrm

data class TripResponse(val code: String, val waypoints: List<Waypoint>, val trips: List<Trip>)