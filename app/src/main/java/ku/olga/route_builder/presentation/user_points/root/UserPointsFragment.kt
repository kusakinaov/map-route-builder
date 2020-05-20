package ku.olga.route_builder.presentation.user_points.root

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment

class UserPointsFragment : BaseFragment() {
    private lateinit var userPointsAdapter: UserPointsAdapter
    private var userPointsView: UserPointsView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_user_points)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPointsAdapter = UserPointsAdapter(childFragmentManager, resources)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_user_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = UserPointsViewImpl(this, userPointsAdapter)
        userPointsView?.onAttach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userPointsView?.onDetach()
    }

    companion object {
        fun newInstance() = UserPointsFragment()
    }
}