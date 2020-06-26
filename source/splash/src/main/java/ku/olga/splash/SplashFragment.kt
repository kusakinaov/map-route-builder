package ku.olga.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_splash.*
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.mediator.MainMediator
import ku.olga.ui_core.view.CompassView
import ku.olga.ui_core.base.BaseFragment
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.min

class SplashFragment : BaseFragment() {
    @Inject
    lateinit var mainMediator: MainMediator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_splash, container, false)

    override fun inject(activity: FragmentActivity) {
        activity.application?.let {
            if (it is AppWithFacade) {
                SplashComponent.build(it.getFacade()).inject(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCompass.onAngleChangeListener = object : CompassView.OnAngleChangeListener {
            override fun onAngleChanged(angle: Double) {
                val progress = (MAX_ANGLE - min(abs(
                    MAX_ANGLE - angle), abs(angle))) / MAX_ANGLE * 100
                viewProgress.setProgress(progress.toInt())

                if (progress >= PERMISSIBLE_VARIATION) {
                    viewCompass.onAngleChangeListener = null
                    activity?.apply {
                        finish()
                        mainMediator.openMainActivity(this)
                    }
                }
            }
        }
    }

    companion object {
        private const val MAX_ANGLE = 360
        private const val PERMISSIBLE_VARIATION = 95
    }
}