package ku.olga.route_builder.presentation.user_points.list

import android.os.Bundle
import android.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.user_points.OnUserPointsChangeListener

class UserPointsListFragment : BaseFragment(), OnUserPointsChangeListener {
    private val userPointsPresenter = UserPointsListPresenter()
    private var userPointsView: UserPointsListView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View =
            inflater.inflate(R.layout.fragment_user_points_list, container, false)

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