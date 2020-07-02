package ku.olga.nominatim.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import ku.olga.nominatim.model.BoundingBox
import ku.olga.nominatim.model.Coordinates

class BoundingBoxTypeAdapter : TypeAdapter<BoundingBox>() {
    override fun write(out: JsonWriter?, value: BoundingBox?) {
        out?.apply {
            value?.let {
                beginArray()
                value(it.southWest.latitude)
                value(it.northEast.latitude)
                value(it.southWest.longitude)
                value(it.northEast.longitude)
                endArray()
            }
        }
    }

    override fun read(input: JsonReader?): BoundingBox? {
        var southWest: Coordinates? = null
        var northEast: Coordinates? = null

        input?.apply {
            beginArray()

            val minLat = nextDouble()
            val maxLat = nextDouble()
            val minLon = nextDouble()
            val maxLon = nextDouble()

            southWest = Coordinates(minLat, minLon)
            northEast = Coordinates(maxLat, maxLon)

            endArray()
        }

        return if (southWest == null || northEast == null)
            null
        else
            BoundingBox(southWest!!, northEast!!)
    }
}