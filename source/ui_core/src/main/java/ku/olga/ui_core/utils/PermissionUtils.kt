package ku.olga.ui_core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun hasLocationPermission(context: Context?): Boolean = if (context != null) {
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
} else false

fun requestLocationPermission(fragment: Fragment, requestCode: Int) {
    fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
}