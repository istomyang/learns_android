package ty.learns.android.advancedgraphics

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.ColorInt
import ty.learns.android.R
import kotlin.math.abs

// https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/
class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val backgroundColor = 0xFFD5F5E3.toInt()
    private val drawColor = 0xFFC39BD3.toInt()

    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true // 是否应用边缘平滑
        isDither = true // 影响设备更高精度的颜色如何被下采样，抖动是将图像的颜色范围减少到 256 种（或更少）颜色的最常用方法。
        style = Paint.Style.STROKE // default: FILL 填充还是描边
        strokeJoin = Paint.Join.ROUND // default: MITER 连结点
        strokeCap = Paint.Cap.ROUND // default: BUTT 拐弯角
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin) 笔粗
    }

    private var path = Path()

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)


        val inset = 40
        val frame = Rect(inset, inset, width - inset, height - inset)

        if (::extraBitmap.isInitialized) extraBitmap.recycle() // 回收

        // 将绘制信息存储到 Bitmap 里，但不是唯一的方式
        // 也可以使用 drawing.addPath(curPath)
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
        extraCanvas.drawRect(frame, paint) // 画外围框框。
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    private var currentTouchX = 0f
    private var currentTouchY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentTouchX = event.x
        currentTouchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        // 子View消费事件的时候，父View屏蔽掉。
        when (event.action) {
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(false)
        }

        return true
    }

    private var startX = 0f
    private var startY = 0f

    private fun touchStart() {
        path.reset()
        path.moveTo(currentTouchX, currentTouchY)
        startX = currentTouchX
        startY = currentTouchY
    }

    // 如果手指几乎没有移动，则无需绘制。
    // 如果手指移动的距离小于该touchTolerance距离，则不要绘制。
    // scaledTouchSlop返回系统认为用户正在滚动之前触摸可以移动的距离（以像素为单位）。
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private fun touchMove() {
        val dx = abs(currentTouchX - startX)
        val dy = abs(currentTouchY - startY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(startX, startY, (currentTouchX + startX) / 2, (currentTouchY + startY) / 2)
            startX = currentTouchX
            startY = currentTouchY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }

    companion object {
        var STROKE_WIDTH = 12f // 笔宽度
    }
}