package ku.olga.ui_core.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    activity?.let { a ->
        (a.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).let { imm ->
            a.currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, 0);
            }
        }
    }
}

fun setKeyboardListener(
    activity: Activity,
    rootLayout: ViewGroup,
    listener: OnKeyboardVisibilityListener?
) {
    rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
        val navigationBarHeight = getNavigationBarHeight(activity)
        val statusBarHeight = getStatusBarHeight(activity)

        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)

        val keyboardHeight =
            rootLayout.rootView.height - (navigationBarHeight + statusBarHeight + rect.height())
        listener?.onKeyboardVisibilityChanged(keyboardHeight > 0)
    }
}


interface OnKeyboardVisibilityListener {
    fun onKeyboardVisibilityChanged(isVisible: Boolean)
}

private const val RESOURCE_NAVIGATION_BAR_HEIGHT = "navigation_bar_height"
private const val RESOURCE_STATUS_BAR_HEIGHT = "status_bar_height"

private fun getNavigationBarHeight(context: Context): Int =
    getDimensionResource(context.resources, RESOURCE_NAVIGATION_BAR_HEIGHT)

private fun getStatusBarHeight(context: Context): Int =
    getDimensionResource(context.resources, RESOURCE_STATUS_BAR_HEIGHT)

private fun getDimensionResource(resources: Resources, name: String): Int =
    resources.getIdentifier(name, "dimen", "android").run {
        return if (this > 0)
            resources.getDimensionPixelSize(this)
        else
            0
    }