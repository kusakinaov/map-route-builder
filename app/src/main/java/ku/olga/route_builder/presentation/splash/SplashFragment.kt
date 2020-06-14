package ku.olga.route_builder.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_splash.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.view.CompassView
import kotlin.math.abs

class SplashFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_splash, container, false)

    override fun inject(activity: FragmentActivity) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCompass.onAngleChangeListener = object : CompassView.OnAngleChangeListener {
            override fun onAngleChanged(angle: Double) {
                if (abs(angle) < 3 || abs(360 - angle) < 3) {
                    viewCompass.onAngleChangeListener = null
                    activity?.let {
                        it.finish()
                        it.startActivity(Intent(it, MainActivity::class.java))
                    }
                }
            }
        }
    }
}