package ku.olga.route_builder.presentation.user_points.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import ku.olga.route_builder.R
import ku.olga.core_api.dto.UserPoint
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.ui_core.base.BaseFragment
import ku.olga.route_builder.presentation.user_points.OnUserPointsChangeListener
import javax.inject.Inject

class UserPointsListFragment : BaseFragment(), OnUserPointsChangeListener {
    @Inject
    lateinit var userPointsPresenter: UserPointsListPresenter

    private var userPointsView: UserPointsListView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View =
            inflater.inflate(R.layout.fragment_user_points_list, container, false)

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = UserPointsListViewImpl(this, userPointsPresenter).apply {
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
}