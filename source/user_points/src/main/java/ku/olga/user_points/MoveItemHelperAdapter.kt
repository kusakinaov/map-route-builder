package ku.olga.user_points

interface MoveItemHelperAdapter {
    fun onItemMove(from: Int, to: Int): Boolean
    fun onItemMoveFinished()
}