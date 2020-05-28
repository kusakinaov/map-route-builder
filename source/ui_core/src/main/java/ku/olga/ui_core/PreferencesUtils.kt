package ku.olga.ui_core

import android.content.SharedPreferences
import ku.olga.core_api.dto.Coordinates

private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val DEFAULT_LATITUDE = 54.180857
private const val DEFAULT_LONGITUDE = 45.186319

private fun SharedPreferences.Editor.putDouble(
    key: String,
    double: Double
): SharedPreferences.Editor =
    putLong(key, java.lang.Double.doubleToRawLongBits(double))

private fun SharedPreferences.getDouble(key: String, defaultDouble: Double): Double =
    java.lang.Double.longBitsToDouble(
        getLong(
            key,
            java.lang.Double.doubleToRawLongBits(defaultDouble)
        )
    )

fun getLastCoordinates(preferences: SharedPreferences) = Coordinates(
    preferences.getDouble(LATITUDE, DEFAULT_LATITUDE),
    preferences.getDouble(LONGITUDE, DEFAULT_LONGITUDE)
)

fun setLastCoordinates(preferences: SharedPreferences, coordinates: Coordinates) {
    preferences.edit().putDouble(LATITUDE, coordinates.latitude)
        .putDouble(LONGITUDE, coordinates.longitude).apply()
}