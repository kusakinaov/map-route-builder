package ku.olga.route_builder.presentation.list

import android.content.res.Resources
import android.os.Bundle
import android.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.dagger.component.DaggerPointComponent
import ku.olga.route_builder.presentation.dagger.component.PointComponent
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule
import javax.inject.Inject

class UserPointsFragment : BaseFragment() {
    private lateinit var pointsComponents: PointComponent
    @Inject
    lateinit var userPointsPresenter: UserPointsPresenter

    private var userPointsView: UserPointsView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_point_list)

    override fun inject() {
        pointsComponents =
            DaggerPointComponent.builder()
                .applicationModule(ApplicationModule(App.application))
                .build()
        pointsComponents.inject(this)
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
        userPointsView = UserPointsViewImpl(this, userPointsPresenter).apply {
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