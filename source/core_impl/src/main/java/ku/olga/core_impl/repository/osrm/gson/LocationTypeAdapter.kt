package ku.olga.core_impl.repository.osrm.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import ku.olga.core_api.dto.Coordinates

class LocationTypeAdapter : TypeAdapter<Coordinates>() {
    override fun write(out: JsonWriter?, value: Coordinates?) {
        out?.apply {
            beginArray()
            value?.let {
                value(it.longitude)
                value(it.latitude)
            }
            endArray()
        }
    }

    override fun read(reader: JsonReader?): Coordinates? = when (reader) {
        null -> null
        else -> {
            reader.beginArray()
            val longitude = reader.nextDouble()
            val latitude = reader.nextDouble()
            reader.endArray()

            Coordinates(latitude, longitude)
        }
    }
}