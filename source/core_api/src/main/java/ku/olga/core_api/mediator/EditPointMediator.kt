package ku.olga.core_api.mediator

import androidx.fragment.app.Fragment
import ku.olga.core_api.dto.UserPoint

interface EditPointMediator {
    fun editPoint(target: Fragment, requestCode: Int, userPoint: UserPoint)
    fun showEditPointDialog(target: Fragment, requestCode: Int, userPoint: UserPoint)
}