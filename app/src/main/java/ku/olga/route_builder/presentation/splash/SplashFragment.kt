package ku.olga.route_builder.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_splash.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.user_points.root.UserPointsFragment
import ku.olga.route_builder.presentation.view.CompassView
import kotlin.math.abs

class SplashFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_splash, container, false)

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCompass.onAngleChangeListener = object : CompassView.OnAngleChangeListener {
            override fun onAngleChanged(angle: Double) {
                if (abs(angle) < 3 || abs(360 - angle) < 3) {
                    replaceFragment(UserPointsFragment.newInstance(), false)
                }
            }
        }
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}