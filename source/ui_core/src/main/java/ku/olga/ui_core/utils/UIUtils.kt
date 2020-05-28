package ku.olga.ui_core.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.TypedValue

fun convertDpToPx(resources: Resources, dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun convertSpToPx(resources: Resources, dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, resources.displayMetrics)

fun getBitmap(drawable: Drawable): Bitmap? =
    Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        .also { bitmap ->
            drawable.apply {
                setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                draw(Canvas().apply {
                    setBitmap(bitmap)
                })
            }
        }