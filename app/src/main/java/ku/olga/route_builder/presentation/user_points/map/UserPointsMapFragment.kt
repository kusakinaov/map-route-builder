package ku.olga.route_builder.presentation.user_points.map

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.user_points.OnUserPointsChangeListener
import javax.inject.Inject

class UserPointsMapFragment : BaseFragment(), OnUserPointsChangeListener {
    @Inject
    lateinit var presenter: UserPointsMapPresenter

    private lateinit var mapView: UserPointsMapView

    override fun getTitle(resources: Resources) = resources.getString(R.string.tab_user_points_map)

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
        inflater.inflate(R.layout.fragment_user_points_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = UserPointsMapViewImpl(this, presenter)
        mapView.onAttach()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach()
    }

    override fun isPressBackConsumed() = mapView.hideUserPoint()

    override fun setTitle() {}

    interface BottomSheetCallback {
        fun onShown()
        fun onHide()
    }

    companion object {
        fun newInstance() = UserPointsMapFragment()
    }

    override fun onUserPointsChanged(userPoints: List<UserPoint>) {
        presenter.setUserPoints(userPoints)
    }
}