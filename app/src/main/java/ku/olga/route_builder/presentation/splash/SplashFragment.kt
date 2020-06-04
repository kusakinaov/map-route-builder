package ku.olga.route_builder.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.user_points.root.UserPointsFragment

class SplashFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_splash, container, false)

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.postDelayed({
//            replaceFragment(UserPointsFragment.newInstance(), false)
//        }, 1000)
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}