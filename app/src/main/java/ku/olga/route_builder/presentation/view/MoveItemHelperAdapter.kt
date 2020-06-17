package ku.olga.route_builder.presentation.view

interface MoveItemHelperAdapter {
    fun onItemMove(from: Int, to: Int): Boolean
    fun onItemMoveFinished()
}