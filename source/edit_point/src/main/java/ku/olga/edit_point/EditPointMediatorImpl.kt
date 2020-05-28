package ku.olga.edit_point

import androidx.fragment.app.Fragment
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.base.BaseFragment
import javax.inject.Inject

class EditPointMediatorImpl @Inject constructor() : EditPointMediator {
    override fun editPoint(target: Fragment, requestCode: Int, userPoint: UserPoint) {
        if (target is BaseFragment) {
            target.replaceFragment(
                EditPointFragment.newInstance(target, requestCode, userPoint),
                true
            )
        }
    }
}