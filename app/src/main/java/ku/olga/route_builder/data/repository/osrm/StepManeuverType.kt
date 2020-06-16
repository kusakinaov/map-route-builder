package ku.olga.route_builder.data.repository.osrm

enum class StepManeuverType(val text: String) {
    turn("turn"),
    new_name("new name"),
    depart("depart"),
    arrive("arrive"),
    merge("merge"),
    ramp("ramp"),
    on_ramp("on ramp"),
    off_ramp("off ramp"),
    fork("fork"),
    end_of_road("end of road"),
    use_lane("use lane"),
    `continue`("continue"),
    roundabout("roundabout"),
    rotary("rotary"),
    roundabout_turn("roundabout turn"),
    notification("notification"),
    exit_roundabout("exit roundabout"),
    exit_rotary("exit rotary")
}