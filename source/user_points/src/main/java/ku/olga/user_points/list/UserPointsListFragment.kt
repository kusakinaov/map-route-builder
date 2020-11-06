package ku.olga.user_points.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.OnUserPointsChangeListener
import ku.olga.user_points.R
import javax.inject.Inject

class UserPointsListFragment : BaseFragment(R.layout.fragment_user_points_list),
    OnUserPointsChangeListener {
    @Inject
    lateinit var userPointsPresenter: UserPointsListPresenter

    @Inject
    lateinit var editPointMediator: EditPointMediator

    private var userPointsView: UserPointsListView? = null

    override fun inject(activity: FragmentActivity) {
        activity.application?.let {
            if (it is AppWithFacade) {
                UserPointsListComponent.build(it.getFacade()).inject(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = buildUserPointsListView(view).apply { onAttach() }
    }

    private fun buildUserPointsListView(view: View) =
        object : UserPointsListViewImpl(view, userPointsPresenter) {
            override fun editPoint(userPoint: UserPoint) {
                this@UserPointsListFragment.editPoint(userPoint)
            }

            override fun invalidateUserPoints() {
                this@UserPointsListFragment.invalidateUserPoints()
            }
        }

    private fun editPoint(userPoint: UserPoint) {
        parentFragment?.let {
            editPointMediator.editPoint(it, REQ_CODE_EDIT_POINT, userPoint)
        }
    }

    private fun invalidateUserPoints() {
        parentFragment?.let {
            if (it is OnOrderChangeCallback) {
                it.onOrderChanged()
            }
        }
    }

    override fun onDestroyView() {
        userPointsView?.onDetach()
        super.onDestroyView()
    }

    override fun setTitle() {}

    companion object {
        fun newInstance() = UserPointsListFragment()
    }

    override fun onUserPointsChanged(userPoints: List<UserPoint>) {
        userPointsPresenter.setUserPoints(userPoints)
    }

    interface OnOrderChangeCallback {
        fun onOrderChanged()
    }
}