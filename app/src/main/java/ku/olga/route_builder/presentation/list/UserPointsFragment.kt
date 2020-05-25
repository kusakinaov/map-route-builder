package ku.olga.route_builder.presentation.list

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_EDIT_POINT
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.point.EditPointFragment
import javax.inject.Inject

class UserPointsFragment : BaseFragment() {
    @Inject
    lateinit var userPointsPresenter: UserPointsPresenter

    @Inject
    lateinit var pointsAdapter: UserPointsAdapter

    private var userPointsView: UserPointsView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_point_list)

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pointsAdapter.onPointClickListener = {
            replaceFragment(
                EditPointFragment.newInstance(this@UserPointsFragment, REQ_CODE_EDIT_POINT, it),
                true
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.points, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_user_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = UserPointsViewImpl(this, userPointsPresenter, pointsAdapter).apply {
            onAttach()
        }
    }

    override fun onDestroyView() {
        userPointsView?.onDetach()
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionMap -> {
            userPointsView?.onClickOpenMap()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}