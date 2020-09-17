package ku.olga.osrm.model

data class TripResponse(val code: String, val waypoints: List<Waypoint>, val trips: List<Route>)