package ku.olga.route_builder.presentation.map

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.dagger.component.DaggerPointComponent
import ku.olga.route_builder.presentation.dagger.component.PointComponent
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule
import javax.inject.Inject

class UserPointsMapFragment : BaseFragment() {
    private lateinit var pointComponent: PointComponent
    @Inject
    lateinit var presenter: UserPointsMapPresenter

    private lateinit var mapView: UserPointsMapView

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_map)

    override fun inject() {
        pointComponent =
            DaggerPointComponent.builder().applicationModule(ApplicationModule(App.application))
                .build()
        pointComponent.inject(this)
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
}