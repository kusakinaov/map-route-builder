package ku.olga.nominatim.model

enum class ZoomLevel(val value: Int) {
    country(3),
    state(5),
    county(8),
    city(10),
    suburb(14),
    major_streets(16),
    major_and_minor_streets(17),
    building(18)
}