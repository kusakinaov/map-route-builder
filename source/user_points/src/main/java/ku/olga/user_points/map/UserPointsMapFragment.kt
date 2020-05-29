package ku.olga.user_points.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.OnUserPointsChangeListener
import ku.olga.user_points.R
import javax.inject.Inject

class UserPointsMapFragment : BaseFragment(),
    OnUserPointsChangeListener {
    @Inject
    lateinit var presenter: UserPointsMapPresenter

    @Inject
    lateinit var editPointMediator: EditPointMediator

    private lateinit var mapView: UserPointsMapView

    override fun inject(activity: FragmentActivity) {
        activity.application?.let {
            if (it is AppWithFacade) {
                UserPointsMapComponent.build(it.getFacade()).inject(this)
            }
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
        mapView = UserPointsMapViewImpl(this, presenter, editPointMediator)
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