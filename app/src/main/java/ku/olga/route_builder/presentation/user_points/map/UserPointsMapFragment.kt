package ku.olga.route_builder.presentation.user_points.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.user_points.OnUserPointsChangeListener

class UserPointsMapFragment : BaseFragment(), OnUserPointsChangeListener {
    private val presenter = UserPointsMapPresenter()
    private lateinit var mapView: UserPointsMapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
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
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mapView.onDetach()
        super.onDestroyView()
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