package ku.olga.route_builder.presentation.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ku.olga.route_builder.R
import kotlin.math.min

class CompassView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var direction: Float = 0f
    private var radius: Float = 0f
    private var centerPoint = PointF()
    private val shadowWidth = convertDpToPx(resources, 4f)
    private val divisionHeight = convertDpToPx(resources, 8f)
    private val arrowHalfWidth = convertDpToPx(resources, 10f)

    private val shadowPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.GRAY
            maskFilter = BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.OUTER)
        }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        strokeWidth = convertDpToPx(resources, 1f)
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL_AND_STROKE
        textSize = convertDpToPx(resources, 16f)
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val textHeight = Rect().apply {
        textPaint.getTextBounds("N", 0, 1, this)
    }.height()

    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL_AND_STROKE
    }
    private val arrowPath = Path()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CompassView)
        try {
            setDirection(typedArray.getFloat(R.styleable.CompassView_compass_direction, 0f))
        } finally {
            typedArray.recycle()
        }
    }

    fun setDirection(direction: Float) {
        this.direction = direction
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(measure(widthMeasureSpec), measure(heightMeasureSpec))
        setMeasuredDimension(size, size)

        radius = size / 2f - shadowWidth
    }

    private fun measure(measureSpec: Int): Int =
        if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            convertDpToPx(resources, DEFAULT_SIZE).toInt()
        } else {
            MeasureSpec.getSize(measureSpec)
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        centerPoint.apply {
            x = width / 2f
            y = height / 2f
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, shadowPaint)
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, backgroundPaint)
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, strokePaint)

        for (i in 0 until 24) {
            if (i % 6 == 0) {
                canvas.drawLine(
                    centerPoint.x,
                    shadowWidth,
                    centerPoint.x,
                    2 * divisionHeight + shadowWidth,
                    strokePaint
                )
            } else {
                canvas.drawLine(
                    centerPoint.x,
                    shadowWidth,
                    centerPoint.x,
                    divisionHeight + shadowWidth,
                    strokePaint
                )
            }
            canvas.rotate(15f, centerPoint.x, centerPoint.y)
        }
        canvas.save()

        val padding = convertDpToPx(resources, 4f)
        textPaint.color = Color.RED
        canvas.drawText(
            "N",
            centerPoint.x - textPaint.measureText("N") / 2,
            centerPoint.y - radius + 2 * divisionHeight + textHeight + padding,
            textPaint
        )

        textPaint.color = Color.DKGRAY
        canvas.drawText(
            "S",
            centerPoint.x - textPaint.measureText("S") / 2,
            centerPoint.y + radius - 2 * divisionHeight - padding,
            textPaint
        )
        canvas.drawText(
            "W",
            centerPoint.x - radius + 2 * divisionHeight + padding,
            centerPoint.y + textHeight / 2,
            textPaint
        )
        canvas.drawText(
            "E",
            centerPoint.x + radius - 2 * divisionHeight - padding - textPaint.measureText("E"),
            centerPoint.y + textHeight / 2,
            textPaint
        )
        canvas.save()

        canvas.rotate(45f, centerPoint.x, centerPoint.y)
        arrowPath.moveTo(centerPoint.x - arrowHalfWidth, centerPoint.y)
        arrowPath.lineTo(centerPoint.x + arrowHalfWidth, centerPoint.y)
        arrowPath.lineTo(
            centerPoint.x,
            centerPoint.y - radius + 2 * divisionHeight + textHeight + 2 * padding
        )
        arrowPath.lineTo(centerPoint.x - arrowHalfWidth, centerPoint.y)
        arrowPath.close()

        arrowPaint.color = Color.BLUE
        canvas.drawPath(arrowPath, shadowPaint)
        canvas.drawPath(arrowPath, arrowPaint)
        canvas.save()

        canvas.rotate(180f, centerPoint.x, centerPoint.y)
        arrowPaint.color = Color.RED
        canvas.drawPath(arrowPath, shadowPaint)
        canvas.drawPath(arrowPath, arrowPaint)

        canvas.drawCircle(centerPoint.x, centerPoint.y, arrowHalfWidth / 2, shadowPaint)
        canvas.drawCircle(centerPoint.x, centerPoint.y, arrowHalfWidth / 2, backgroundPaint)
    }

    companion object {
        private const val DEFAULT_SIZE = 100f

        fun convertDpToPx(resources: Resources, dp: Float) =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}