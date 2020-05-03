package ku.olga.route_builder.presentation

import android.content.res.Resources
import android.util.TypedValue

fun convertDpToPx(resources: Resources, dp: Float) = TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)