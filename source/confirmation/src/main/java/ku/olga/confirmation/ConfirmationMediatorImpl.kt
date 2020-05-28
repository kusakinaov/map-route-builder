package ku.olga.confirmation

import android.os.Bundle
import androidx.fragment.app.Fragment
import ku.olga.core_api.mediator.ConfirmationMediator
import javax.inject.Inject

class ConfirmationMediatorImpl @Inject constructor() : ConfirmationMediator {
    override fun showConfirmation(target: Fragment, requestCode: Int, message: String, extras: Bundle?) {
        ConfirmationDialog.show(target, requestCode, message, extras)
    }
}