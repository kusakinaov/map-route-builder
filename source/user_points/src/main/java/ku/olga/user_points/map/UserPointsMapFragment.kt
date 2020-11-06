package ku.olga.user_points.map

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.OnUserPointsChangeListener
import ku.olga.user_points.R
import org.osmdroid.config.Configuration
import javax.inject.Inject

class UserPointsMapFragment : BaseFragment(R.layout.fragment_user_points_map),
    OnUserPointsChangeListener {
    @Inject
    lateinit var presenter: UserPointsMapPresenter

    @Inject
    lateinit var editPointMediator: EditPointMediator

    @Inject
    lateinit var preferences: SharedPreferences

    private lateinit var mapView: UserPointsMapView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Configuration.getInstance().load(context, preferences)

        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!mapView.hideUserPoint()) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    override fun inject(activity: FragmentActivity) {
        activity.application?.let {
            if (it is AppWithFacade) {
                UserPointsMapComponent.build(it.getFacade()).inject(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = buildUserPointsMapView(view)
        mapView.onAttach()
    }

    private fun buildUserPointsMapView(view: View) =
        object : UserPointsMapViewImpl(view, presenter, bottomSheetCallback) {
            override fun editUserPoint(userPoint: UserPoint) {
                this@UserPointsMapFragment.editUserPoint(userPoint)
            }
        }

    val bottomSheetCallback: BottomSheetCallback?
        get() = if (parentFragment is BottomSheetCallback) parentFragment as BottomSheetCallback else null

    private fun editUserPoint(userPoint: UserPoint) {
        parentFragment?.let {
            editPointMediator.editPoint(it, REQ_CODE_EDIT_POINT, userPoint)
        }
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