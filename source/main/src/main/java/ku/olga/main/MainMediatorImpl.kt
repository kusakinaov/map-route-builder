package ku.olga.main

import android.app.Activity
import android.content.Intent
import ku.olga.core_api.mediator.MainMediator
import javax.inject.Inject

class MainMediatorImpl @Inject constructor() : MainMediator {
    override fun openMainActivity(activity: Activity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))

        activity.overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
    }
}