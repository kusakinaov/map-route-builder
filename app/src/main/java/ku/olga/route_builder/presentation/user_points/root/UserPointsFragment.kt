package ku.olga.route_builder.presentation.user_points.root

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.mediator.SearchMediator
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.map.UserPointsMapFragment
import javax.inject.Inject

class UserPointsFragment : BaseFragment(), UserPointsMapFragment.BottomSheetCallback {
    @Inject
    lateinit var presenter: UserPointsPresenter

    @Inject
    lateinit var searchMediator: SearchMediator

    private lateinit var userPointsAdapter: UserPointsAdapter
    private var userPointsView: UserPointsView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_user_points)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPointsAdapter = UserPointsAdapter(childFragmentManager, resources)
    }

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_user_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = UserPointsViewImpl(this, presenter, userPointsAdapter, searchMediator)
        userPointsView?.onAttach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userPointsView?.onDetach()
    }

    override fun isPressBackConsumed() = userPointsView?.isPressBackConsumed() ?: false

    companion object {
        fun newInstance() = UserPointsFragment()
    }

    override fun onShown() {
        userPointsView?.onShown()
    }

    override fun onHide() {
        userPointsView?.onHide()
    }
}