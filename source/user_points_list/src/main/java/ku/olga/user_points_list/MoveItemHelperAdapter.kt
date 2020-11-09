package ku.olga.user_points_list

interface MoveItemHelperAdapter {
    fun onItemMove(from: Int, to: Int): Boolean
    fun onItemMoveFinished()
}