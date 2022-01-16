package com.example.androidschool.andersenhomeworks.lesson4

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.withSave
import com.example.androidschool.andersenhomeworks.R
import java.util.*

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
): View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val DEFAULT_SIZE = 40
        const val DEFAULT_WATCHFACE_THICKNESS = 2
        const val DEFAULT_HOUR_HAND_THICKNESS = 2
        const val DEFAULT_MINUTE_HAND_THICKNESS = 2
        const val DEFAULT_SECOND_HAND_THICKNESS = 2
        const val DEFAULT_DOT_THICKNESS = 5
        const val DEFAULT_WATCHFACE_COLOR = Color.BLACK
        const val DEFAULT_HOUR_HAND_COLOR = Color.DKGRAY
        const val DEFAULT_MINUTE_HAND_COLOR = Color.BLUE
        const val DEFAULT_SECOND_HAND_COLOR = Color.RED
        const val DEFAULT_DOT_COLOR = Color.BLACK
    }

    private val calendar = Calendar.getInstance()
    private var currentSeconds = calendar.get(Calendar.SECOND)
    private var currentMinutes = calendar.get(Calendar.MINUTE)
    private var currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    private var handHourLength = 5f
    private var minuteHourLength = 5f
    private var secondHourLength = 5f

    private var watchfaceThickness: Float = context.dpToPx(DEFAULT_WATCHFACE_THICKNESS) * 2
    private var watchfaceRadius = (width / 2f) - watchfaceThickness

    private var watchfaceCirclePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var watchfaceDotsPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = DEFAULT_DOT_THICKNESS.toFloat()
        color = DEFAULT_DOT_COLOR
    }
    private var hourHandPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var minuteHandPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var secondHandPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView)

            watchfaceCirclePaint.apply {
                strokeWidth = typedArray.getDimension(R.styleable.ClockView_watchface_thickness, context.dpToPx(DEFAULT_WATCHFACE_THICKNESS))
                color = typedArray.getColor(R.styleable.ClockView_watchface_color, DEFAULT_WATCHFACE_COLOR)
            }

            hourHandPaint.apply {
                strokeWidth = typedArray.getDimension(R.styleable.ClockView_hourHand_thickness, context.dpToPx(DEFAULT_HOUR_HAND_THICKNESS))
                color = typedArray.getColor(R.styleable.ClockView_hourHand_color, DEFAULT_HOUR_HAND_COLOR)
            }

            minuteHandPaint.apply {
                strokeWidth = typedArray.getDimension(R.styleable.ClockView_minuteHand_thickness, context.dpToPx(DEFAULT_MINUTE_HAND_THICKNESS))
                color = typedArray.getColor(R.styleable.ClockView_minuteHand_color, DEFAULT_MINUTE_HAND_COLOR)
            }

            secondHandPaint.apply {
                strokeWidth = typedArray.getDimension(R.styleable.ClockView_secondHand_thickness, context.dpToPx(DEFAULT_SECOND_HAND_THICKNESS))
                color = typedArray.getColor(R.styleable.ClockView_secondHand_color, DEFAULT_SECOND_HAND_COLOR)
            }

            typedArray.recycle()
        }
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec)
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec)
            else -> MeasureSpec.getSize(spec)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(resolveDefaultSize(widthMeasureSpec), resolveDefaultSize(heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f

        canvas?.apply {
            initWatchface(this, cx, cy)
            drawStrokes(this, watchfaceDotsPaint, watchfaceRadius)
            drawCircle(0f, 0f, watchfaceRadius, watchfaceCirclePaint)
            drawHourHand(this, hourHandPaint, currentHour, currentMinutes)
            drawMinuteHand(this, minuteHandPaint, currentMinutes)
            drawSecondHand(this, secondHandPaint, currentSeconds)
            drawCircle(0f, 0f, watchfaceThickness, watchfaceDotsPaint)
        }

        update()
        Log.e("DRAW", "onDraw")
    }

    private fun update() {
        updateTime()
        postInvalidateDelayed(1000)
    }

    private fun updateTime() {
        if (currentSeconds < 60) {
            currentSeconds++
        } else {
            currentSeconds = 1
            if (currentMinutes < 60) {
                currentMinutes++
            } else {
                currentMinutes = 1
                if (currentHour < 12) {
                    currentHour++
                } else {
                    currentHour = 1
                }
            }
        }
    }

    /**
     * Changes default canvas's coordinate system on comfortable for clock drawing
     */
    private fun initWatchface(canvas: Canvas, cx: Float, cy: Float) {
        canvas.apply {
            scale(1f, -1f, cx, cy)
            translate(cx, cy)
        }
    }

    /**
     * Draws hour strokes on watchface
     */
    private fun drawStrokes(canvas: Canvas, paint: Paint, startY: Float) {
        val endY = startY - (paint.strokeWidth * 5)
        canvas.withSave {
            for (i in 0..11) {
                this.drawLine(0f, startY, 0f, endY, paint)
                this.rotate(30f)
            }
        }
    }

    /**
     * Draws hour hand by hours given
     */
    private fun drawHourHand(canvas: Canvas, paint: Paint, hours: Int, minutes: Int) {
        canvas.withSave {
            this.rotate(hours * -30f + minutes * -0.5f)
            this.drawLine(0f, 0f, 0f, height / 4f, paint)
        }
    }

    /**
     * Draws minute hand by minutes given
     */
    private fun drawMinuteHand(canvas: Canvas, paint: Paint, minutes: Int) {
        canvas.withSave {
            this.rotate(minutes * -6f)
            this.drawLine(0f, 0f, 0f, height / 3f, paint)
        }
    }

    /**
     * Draws second hand by seconds given
     */
    private fun drawSecondHand(canvas: Canvas, paint: Paint, seconds: Int) {
        canvas.withSave {
            this.rotate(seconds * -6f)
            this.drawLine(0f, 0f, 0f, height / 2.5f, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        handHourLength = width / 4f
        minuteHourLength = width / 3f
        secondHourLength = width / 2f
        watchfaceRadius = width / 2f - watchfaceThickness * 2
    }
}

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}