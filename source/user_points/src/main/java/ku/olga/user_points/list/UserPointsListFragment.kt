package ku.olga.user_points.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.OnUserPointsChangeListener
import ku.olga.user_points.R
import javax.inject.Inject

class UserPointsListFragment : BaseFragment(R.layout.fragment_user_points_list), OnUserPointsChangeListener {
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
        userPointsView =
            UserPointsListViewImpl(this, userPointsPresenter, editPointMediator).apply {
                onAttach()
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