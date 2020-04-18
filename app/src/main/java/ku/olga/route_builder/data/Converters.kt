package ku.olga.route_builder.data

import android.location.Address
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.model.UserPoint
import java.lang.StringBuilder
import ku.olga.route_builder.data.room.entity.UserPoint as RoomUserPoint

fun Address.toSearchAddress() = SearchAddress(buildPostalAddress(), latitude, longitude)

fun Address.buildPostalAddress(): String {
    val builder = StringBuilder()
    for (i in 0..maxAddressLineIndex) {
        if (builder.isNotEmpty()) builder.append(", ")
        builder.append(getAddressLine(i))
    }
    return builder.toString()
}

fun UserPoint.toRoomUserPoint() = RoomUserPoint(id, title, postalAddress, lat, lon, description)

fun RoomUserPoint.toUserPoint() = UserPoint(id, title, postalAddress, lat, lon, description)