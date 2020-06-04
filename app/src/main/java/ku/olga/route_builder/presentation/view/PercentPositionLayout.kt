package ku.olga.route_builder.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.children
import ku.olga.route_builder.R

class PercentPositionLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        Log.d(TAG, "onMeasure: $width, $height")

        for (child in children) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var layoutParams: PercentLayoutParams

        var left: Int
        var top: Int
        var right: Int
        var bottom: Int

        var horizontalPosition: Int
        var verticalPosition: Int

        for (child in children) {
            layoutParams = child.layoutParams as PercentLayoutParams

            left = child.measuredWidth / 2
            right = width - left
            horizontalPosition = (right - left) * layoutParams.horizontalPositionPercent / 100

            top = child.measuredHeight / 2
            bottom = height - top
            verticalPosition = (bottom - top) * layoutParams.verticalPositionPercent / 100

            child.layout(horizontalPosition, verticalPosition, horizontalPosition + child.measuredWidth, verticalPosition + child.measuredHeight)
        }
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean = p is LayoutParams

    override fun generateDefaultLayoutParams(): LayoutParams =
            PercentLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams = PercentLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams): LayoutParams = PercentLayoutParams(p)

    class PercentLayoutParams : LayoutParams {
        var verticalPositionPercent: Int = 0
        var horizontalPositionPercent: Int = 0

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
            val typedArray = c.obtainStyledAttributes(attrs, R.styleable.PercentPositionLayout_Layout)
            try {
                horizontalPositionPercent = typedArray.getInt(R.styleable.PercentPositionLayout_Layout_layout_horizontal_position_percent, 0)
                verticalPositionPercent = typedArray.getInt(R.styleable.PercentPositionLayout_Layout_layout_vertical_position_percent, 0)
            } finally {
                typedArray.recycle()
            }
        }

        constructor(layoutParams: LayoutParams) : super(layoutParams)
        constructor(width: Int, height: Int) : super(width, height)
    }

    companion object {
        private val TAG = PercentPositionLayout::class.java.simpleName
    }
}