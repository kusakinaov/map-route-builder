package ku.olga.route_builder.presentation

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object KeyboardUtils {

    fun Fragment.hideKeyboard() {
        activity?.let { a ->
            (a.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).let { imm ->
                a.currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0);
                }
            }
        }
    }
}