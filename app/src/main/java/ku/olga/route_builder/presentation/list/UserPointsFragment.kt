package ku.olga.route_builder.presentation.list

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_SEARCH_POINT
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment

class UserPointsFragment : BaseFragment() {
    private val userPointsPresenter = UserPointsPresenter(App.pointsRepository)
    private var userPointsView: UserPointsView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_point_list)

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
        inflater.inflate(R.layout.fragment_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = UserPointsViewImpl(this, view, userPointsPresenter).apply {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SEARCH_POINT) {

            }
        }
    }
}