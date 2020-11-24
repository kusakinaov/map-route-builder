package ku.olga.ui_core.utils

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan

fun highlight(
    string: CharSequence?, subString: CharSequence?,
    color: Int, ignoreCase: Boolean = true
): Spanned? =
    if (string == null)
        null
    else
        SpannableStringBuilder(string).apply {
            subString?.split("\\s+".toRegex())?.forEach {
                indexOf(it, ignoreCase = ignoreCase).let { index ->
                    if (index >= 0) {
                        setSpan(
                            ForegroundColorSpan(color),
                            index,
                            index + it.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }