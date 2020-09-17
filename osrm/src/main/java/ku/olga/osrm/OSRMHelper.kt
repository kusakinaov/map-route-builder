package ku.olga.osrm

import ku.olga.osrm.model.Coordinates
import ku.olga.osrm.model.Profile
import ku.olga.osrm.model.TripResponse

object OSRMHelper {
    fun getFootDirections(points: List<Coordinates>): List<Coordinates> =
        mutableListOf<Coordinates>().also {
            fillCoordinates(DirectionsService.getTrip(points, Profile.foot), it)
        }

    fun getCarDirections(points: List<Coordinates>): List<Coordinates> =
        mutableListOf<Coordinates>().also {
            fillCoordinates(DirectionsService.getTrip(points, Profile.car), it)
        }

    fun getBikeDirections(points: List<Coordinates>): List<Coordinates> =
        mutableListOf<Coordinates>().also {
            fillCoordinates(DirectionsService.getTrip(points, Profile.bike), it)
        }

    private fun fillCoordinates(tripResponse: TripResponse, coordinates: MutableList<Coordinates>) {
        tripResponse.trips.let {
            if (it.isNotEmpty()) {
                for (leg in it[0].legs) {
                    for (step in leg.steps) {
                        for (intersection in step.intersections) {
                            coordinates.add(intersection.location)
                        }
                    }
                }
            }
        }
    }
}