package ku.olga.route_builder.presentation.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ku.olga.route_builder.R
import kotlin.math.min

class CompassView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var onAngleChangeListener: OnAngleChangeListener? = null

    private var radius: Float = 0f
    private var centerPoint = PointF()
    private val shadowWidth = convertDpToPx(resources, 4f)
    private val strokeWidth = convertDpToPx(resources, 8f)
    private val divisionHeight = convertDpToPx(resources, 8f)
    private val arrowHalfWidth = convertDpToPx(resources, 10f)

    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.GRAY
        maskFilter = BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.OUTER)
    }
    private val internalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        isDither = true
    }
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
    }
    private val divisionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = convertDpToPx(resources, 1f)
        style = Paint.Style.STROKE
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        strokeWidth = this@CompassView.strokeWidth
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL_AND_STROKE
        textSize = convertDpToPx(resources, 20f)
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL_AND_STROKE
    }

    private val textHeight = Rect().apply {
        textPaint.getTextBounds("N", 0, 1, this)
    }.height()
    private val arrowPath = Path()

    private val sensorManager: SensorManager
    private val sensorMagnetic: Sensor?
    private val sensorGravity: Sensor?

    private val rotation = FloatArray(9)
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val orientation = FloatArray(3)

    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            processSensorEvent(event)
            startedListenToSensor = true
        }
    }
    private var angle: Double = 0.0
    private var startedListenToSensor = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CompassView)
        try {
        } finally {
            typedArray.recycle()
        }

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(measure(widthMeasureSpec), measure(heightMeasureSpec))
        setMeasuredDimension(size, size)

        radius = size / 2f - shadowWidth - strokeWidth
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
        internalPaint.shader = buildBackgroundGradient()
        strokePaint.shader = buildShadowGradient()
    }

    private fun buildBackgroundGradient() = LinearGradient(
        centerPoint.x, centerPoint.y - radius,
        centerPoint.x, centerPoint.y,
        Color.LTGRAY, Color.WHITE, Shader.TileMode.CLAMP
    ).also {
        it.setLocalMatrix(Matrix().apply { setRotate(-45.0f, centerPoint.x, centerPoint.y) })
    }

    private fun buildShadowGradient() = LinearGradient(
        centerPoint.x, centerPoint.y - radius - strokeWidth,
        centerPoint.x, centerPoint.y,
        Color.GRAY, Color.LTGRAY, Shader.TileMode.CLAMP
    ).also {
        it.setLocalMatrix(Matrix().apply { setRotate(-45.0f, centerPoint.x, centerPoint.y) })
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius + strokeWidth, shadowPaint)
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius + strokeWidth / 2, strokePaint)
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, internalPaint)

        for (i in 0 until 24) {
            if (i % 6 == 0) {
                canvas.drawLine(
                    centerPoint.x,
                    shadowWidth + strokeWidth,
                    centerPoint.x,
                    2 * divisionHeight + shadowWidth + strokeWidth,
                    divisionPaint
                )
            } else {
                canvas.drawLine(
                    centerPoint.x,
                    shadowWidth + strokeWidth,
                    centerPoint.x,
                    divisionHeight + shadowWidth + strokeWidth,
                    divisionPaint
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

        canvas.rotate(-angle.toFloat(), centerPoint.x, centerPoint.y)
        arrowPath.moveTo(centerPoint.x - arrowHalfWidth, centerPoint.y)
        arrowPath.lineTo(centerPoint.x + arrowHalfWidth, centerPoint.y)
        arrowPath.lineTo(
            centerPoint.x,
            centerPoint.y - radius + 2 * divisionHeight + textHeight + 2 * padding
        )
        arrowPath.lineTo(centerPoint.x - arrowHalfWidth, centerPoint.y)
        arrowPath.close()

        arrowPaint.color = Color.RED
        canvas.drawPath(arrowPath, shadowPaint)
        canvas.drawPath(arrowPath, arrowPaint)
        canvas.save()

        canvas.rotate(180f, centerPoint.x, centerPoint.y)
        arrowPaint.color = Color.WHITE
        canvas.drawPath(arrowPath, shadowPaint)
        canvas.drawPath(arrowPath, arrowPaint)

        canvas.drawCircle(centerPoint.x, centerPoint.y, arrowHalfWidth / 2, shadowPaint)
        canvas.drawCircle(centerPoint.x, centerPoint.y, arrowHalfWidth / 2, backgroundPaint)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            registerSensorListeners()
        } else {
            unregisterSensorListeners()
        }
    }

    private fun registerSensorListeners() {
        sensorMagnetic?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorGravity?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun unregisterSensorListeners() {
        startedListenToSensor = false
        sensorMagnetic?.let {
            sensorManager.unregisterListener(sensorEventListener)
        }
        sensorGravity?.let {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    private fun processSensorEvent(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_GRAVITY) it.values.copyInto(gravity)
            if (it.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) it.values.copyInto(geomagnetic)
            SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic)
            SensorManager.getOrientation(rotation, orientation)
            setAngle(orientation[0].toDouble())
            invalidate()
        }
    }

    private fun setAngle(radians: Double) {
        angle = Math.toDegrees(radians)
        if (startedListenToSensor) {
            onAngleChangeListener?.onAngleChanged(angle)
        }
    }

    interface OnAngleChangeListener {
        fun onAngleChanged(angle: Double)
    }

    companion object {
        private const val DEFAULT_SIZE = 100f

        fun convertDpToPx(resources: Resources, dp: Float) =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}