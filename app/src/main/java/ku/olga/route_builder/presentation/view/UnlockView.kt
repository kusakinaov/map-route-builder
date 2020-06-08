package ku.olga.route_builder.presentation.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.convertDpToPx
import kotlin.random.Random

class UnlockView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }
    private val buttonPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = convertDpToPx(context.resources, 1f)
    }
    private val path: Path = Path()
    private val random = Random(System.currentTimeMillis())

    private var icon: Bitmap? = null
    private val buttonRect: Rect = Rect()
    private val iconRect = Rect()
    private var buttonRadius: Float = 0f

    private var iconMatrix: Matrix? = null

    private val centerPoint = Point()
    private val touchPoint = Point()
    private val movePoint = Point()

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return true
        }
    })

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnlockView)
        try {
            buttonPaint.color = typedArray.getColor(R.styleable.UnlockView_button_color, Color.BLACK)
            icon = getIcon(typedArray.getDrawable(R.styleable.UnlockView_android_src)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                colorFilter = PorterDuffColorFilter(typedArray
                        .getColor(R.styleable.UnlockView_icon_color, Color.BLACK), PorterDuff.Mode.SRC_IN)
            })?.also {
                iconMatrix = Matrix()
            }
            buttonRadius = typedArray.getDimension(R.styleable.UnlockView_button_radius, convertDpToPx(context.resources, 24f))
            shadowPaint.apply {
                maskFilter = BlurMaskFilter(convertDpToPx(context.resources, 4f), BlurMaskFilter.Blur.OUTER)
            }
        } finally {
            typedArray.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        centerPoint.set((right - left) / 2, (bottom - top) / 2)

        buttonRect.let {
            it.left = (centerPoint.x - buttonRadius).toInt()
            it.top = (centerPoint.y - buttonRadius).toInt()
            it.right = (centerPoint.x + buttonRadius).toInt()
            it.bottom = (centerPoint.y + buttonRadius).toInt()
        }

        val halfIconWidth = (icon?.width ?: 0) / 2
        val halfIconHeight = (icon?.height ?: 0) / 2
        iconRect.let {
            it.left = centerPoint.x - halfIconWidth
            it.top = centerPoint.y - halfIconHeight
            it.right = centerPoint.x + halfIconWidth
            it.bottom = centerPoint.y + halfIconHeight
        }
    }

    private fun getIcon(drawable: Drawable?): Bitmap? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && drawable is VectorDrawable) {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                        .apply {
                            Canvas(this).apply { drawable.draw(this) }
                        }
            } else if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else null

    override fun onDraw(canvas: Canvas) {
//        path.cubicTo(0f, random.nextInt(height).toFloat(), random.nextInt(width).toFloat(), random.nextInt(height).toFloat(), width.toFloat(), random.nextInt(height).toFloat())
//        path.moveTo(random.nextInt(width).toFloat(), random.nextInt(height).toFloat())
//        path.cubicTo(0f, random.nextInt(height).toFloat(), random.nextInt(width).toFloat(), random.nextInt(height).toFloat(), width.toFloat(), random.nextInt(height).toFloat())
//        path.moveTo(random.nextInt(width).toFloat(), random.nextInt(height).toFloat())
//        path.cubicTo(0f, random.nextInt(height).toFloat(), random.nextInt(width).toFloat(), random.nextInt(height).toFloat(), width.toFloat(), random.nextInt(height).toFloat())
//        path.moveTo(random.nextInt(width).toFloat(), random.nextInt(height).toFloat())
//
//        canvas.drawPath(path, paint)

        icon?.let { bitmap ->
            canvas.drawCircle(buttonRect.centerX().toFloat(), buttonRect.centerY().toFloat(), buttonRadius, shadowPaint)
            canvas.drawCircle(buttonRect.centerX().toFloat(), buttonRect.centerY().toFloat(), buttonRadius, buttonPaint)
            canvas.drawBitmap(bitmap, null, iconRect, buttonPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !buttonRect.contains(event.x.toInt(), event.y.toInt()))
            return false

        gestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPoint.set(event.x.toInt(), event.y.toInt())
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_CANCEL -> {
            }
            MotionEvent.ACTION_OUTSIDE -> {
            }
        }
        return true
    }
}