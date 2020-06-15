package ku.olga.route_builder.presentation.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.convertDpToPx
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ProgressView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val backgroundPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL_AND_STROKE }
    private val shadowPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val trackPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL_AND_STROKE }

    private val backgroundRect = RectF()
    private val trackRect = RectF()

    private var shadowWidth: Float = 0f
    private var trackHeight: Float = 0f
    private var corners: Float = 0f

    private var progress: Int = 0
    private var progressPercentPx: Float = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
        try {
            setBackgroundTrackColor(
                typedArray.getColor(
                    R.styleable.ProgressView_progress_background_track_color,
                    Color.WHITE
                )
            )
            setTrackHeight(
                typedArray.getDimension(
                    R.styleable.ProgressView_progress_track_height,
                    convertDpToPx(context.resources, DEFAULT_TRACK_HEIGHT)
                )
            )
            setTrackColor(
                typedArray.getColor(
                    R.styleable.ProgressView_progress_track_color,
                    Color.BLACK
                )
            )
            setShadowWidth(
                typedArray.getDimension(
                    R.styleable.ProgressView_progress_shadow_width,
                    convertDpToPx(resources, DEFAULT_SHADOW_HEIGHT)
                )
            )
            setProgress(typedArray.getInt(R.styleable.ProgressView_android_progress, 0))
        } finally {
            typedArray.recycle()
        }
    }

    fun setShadowWidth(width: Float) {
        shadowWidth = width
        shadowPaint.maskFilter = BlurMaskFilter(width, BlurMaskFilter.Blur.OUTER)
        invalidate()
    }

    fun setTrackHeight(height: Float) {
        this.trackHeight = height
        corners = height / 2
        requestLayout()
        invalidate()
    }

    fun setBackgroundTrackColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    fun setTrackColor(color: Int) {
        shadowPaint.color = color
        trackPaint.color = color
        invalidate()
    }

    fun setProgress(progress: Int) {
        if (progressPercentPx == 0f) {
            this.progress = progress
        } else {
            setProgressWidth((progress * progressPercentPx).toInt())
        }
    }

//    private fun buildAnimator(from: Int, to: Int, step: Float) = ValueAnimator.ofInt(
//        (step * from).toInt(),
//        (step * to).toInt()
//    ).setDuration((abs(from - to) * 40).toLong()).apply {
//        addUpdateListener {
//            setProgressWidth(it.animatedValue as Int)
//        }
//    }

    private fun calculateProgressRight(newWidth: Float) = min(
        backgroundRect.right,
        max(backgroundRect.left, newWidth + backgroundRect.left)
    )

    private fun setProgressWidth(width: Int) {
        this.progress = (width / progressPercentPx).toInt()
        trackRect.right = calculateProgressRight(width.toFloat())
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            (trackHeight + 2 * shadowWidth).toInt()
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        backgroundRect.set(
            shadowWidth,
            shadowWidth,
            width.toFloat() - shadowWidth,
            height.toFloat() - shadowWidth
        )
        progressPercentPx = backgroundRect.width() / 100f
        trackRect.set(
            backgroundRect.left,
            backgroundRect.top,
            calculateProgressRight(progressPercentPx * progress),
            backgroundRect.bottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(backgroundRect, corners, corners, backgroundPaint)
        canvas.drawRoundRect(trackRect, corners, corners, shadowPaint)
        canvas.drawRoundRect(trackRect, corners, corners, trackPaint)
    }

    companion object {
        private const val DEFAULT_TRACK_HEIGHT = 24f
        private const val DEFAULT_SHADOW_HEIGHT = 4f
    }
}