package ku.olga.nominatim.model

enum class OsmType(val char: String) {
    node("N"), way("W"), relation("R")
}