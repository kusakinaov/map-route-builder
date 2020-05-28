package ku.olga.core_api.mediator

import android.os.Bundle
import androidx.fragment.app.Fragment

interface ConfirmationMediator {
    fun showConfirmation(target: Fragment, requestCode: Int, message: String, extras: Bundle?)
}