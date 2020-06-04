package ku.olga.route_builder.presentation.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.convertDpToPx
import kotlin.random.Random

class SplashView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint: Paint = Paint().apply {
//        pathEffect = DiscretePathEffect(10f, 10f)
        style = Paint.Style.STROKE
        strokeWidth = convertDpToPx(context.resources, 1f)
    }
    private val path: Path = Path()
    private val random = Random(System.currentTimeMillis())

    private var icon: Bitmap? = null
    private var iconRect: Rect? = null
    private var iconMatrix: Matrix? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SplashView)
        try {
            paint.color = typedArray.getColor(R.styleable.SplashView_path_color, Color.BLACK)
            icon = getIcon(typedArray.getDrawable(R.styleable.SplashView_android_src)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                colorFilter = PorterDuffColorFilter(typedArray
                        .getColor(R.styleable.SplashView_icon_color, Color.BLACK), PorterDuff.Mode.SRC_IN)
            })?.also {
                iconRect = Rect()
                iconMatrix = Matrix()
            }
        } finally {
            typedArray.recycle()
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

        val viewWidth = width
        val viewHeight = height
        iconRect?.apply {
            left = random.nextInt(viewWidth - (icon?.width ?: 0))
            top = random.nextInt(viewHeight - (icon?.height ?: 0))
            right = left + (icon?.width ?: 0)
            bottom = top + (icon?.height ?: 0)
        }

        icon?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, iconRect!!, paint)
            postInvalidateDelayed(500)
        }
    }
}