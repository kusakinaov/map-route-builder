package ku.olga.route_builder.presentation.user_points.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment

class UserPointsMapFragment : BaseFragment() {
    private val presenter = UserPointsMapPresenter(App.pointsRepository)
    private lateinit var mapView: UserPointsMapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_user_points_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = UserPointsMapViewImpl(this, presenter)
        mapView.onAttach(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        mapView.onDetach()
        super.onDestroyView()
    }

    override fun isPressBackConsumed() = mapView.hideBottomSheet()

    override fun setTitle() {}

    companion object {
        fun newInstance() = UserPointsMapFragment()
    }
}