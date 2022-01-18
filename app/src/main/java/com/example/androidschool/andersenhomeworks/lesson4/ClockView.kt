package com.example.androidschool.andersenhomeworks.lesson4

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.lesson4.util.dpToPx
import java.util.*

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val DEFAULT_WATCHFACE_THICKNESS = 2
        const val DEFAULT_HOUR_HAND_THICKNESS = 2
        const val DEFAULT_MINUTE_HAND_THICKNESS = 2
        const val DEFAULT_SECOND_HAND_THICKNESS = 2
        const val DEFAULT_WATCHFACE_COLOR = Color.BLACK
        const val DEFAULT_HOUR_HAND_COLOR = Color.DKGRAY
        const val DEFAULT_MINUTE_HAND_COLOR = Color.BLUE
        const val DEFAULT_SECOND_HAND_COLOR = Color.RED
    }

    private var currentSeconds = 0


    private var currentMinutes = 0
    private var currentHour = 0
    private var cX = 0f

    private var cY = 0f
    private var hourHandLength = 0f

    private var minuteHandLength = 0f
    private var secondHandLength = 0f
    private var watchfaceRadius = 0f

    // Paints
    private var watchfacePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = DEFAULT_WATCHFACE_COLOR
        strokeWidth = DEFAULT_WATCHFACE_THICKNESS.toFloat()
    }
    private var centerDotPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = DEFAULT_WATCHFACE_COLOR
    }
    private var hourHandPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = DEFAULT_HOUR_HAND_COLOR
    }
    private var minuteHandPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = DEFAULT_MINUTE_HAND_COLOR
    }
    private var secondHandPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = DEFAULT_SECOND_HAND_COLOR
    }

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView)

            watchfacePaint.apply {
                strokeWidth = typedArray.getDimension(
                    R.styleable.ClockView_watchface_thickness,
                    context.dpToPx(DEFAULT_WATCHFACE_THICKNESS)
                )
                color = typedArray.getColor(
                    R.styleable.ClockView_watchface_color,
                    DEFAULT_WATCHFACE_COLOR
                )
            }

            centerDotPaint.apply {
                color = typedArray.getColor(
                    R.styleable.ClockView_watchface_color,
                    DEFAULT_WATCHFACE_COLOR
                )
            }

            hourHandPaint.apply {
                strokeWidth = typedArray.getDimension(
                    R.styleable.ClockView_hourHand_thickness,
                    context.dpToPx(DEFAULT_HOUR_HAND_THICKNESS)
                )
                color = typedArray.getColor(
                    R.styleable.ClockView_hourHand_color,
                    DEFAULT_HOUR_HAND_COLOR
                )
            }

            minuteHandPaint.apply {
                strokeWidth = typedArray.getDimension(
                    R.styleable.ClockView_minuteHand_thickness,
                    context.dpToPx(DEFAULT_MINUTE_HAND_THICKNESS)
                )
                color = typedArray.getColor(
                    R.styleable.ClockView_minuteHand_color,
                    DEFAULT_MINUTE_HAND_COLOR
                )
            }

            secondHandPaint.apply {
                strokeWidth = typedArray.getDimension(
                    R.styleable.ClockView_secondHand_thickness,
                    context.dpToPx(DEFAULT_SECOND_HAND_THICKNESS)
                )
                color = typedArray.getColor(
                    R.styleable.ClockView_secondHand_color,
                    DEFAULT_SECOND_HAND_COLOR
                )
            }

            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveDefaultSize(widthMeasureSpec, heightMeasureSpec),
            resolveDefaultSize(widthMeasureSpec, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateDimensions()
    }

    override fun onDraw(canvas: Canvas?) {
        updateTime()
        canvas?.apply {
            initWatchface(this, cX, cY)
            drawWatchface(this, watchfacePaint, watchfaceRadius)
            drawStrokes(this, watchfacePaint, watchfaceRadius)
            drawHourHand(this, hourHandPaint, currentHour, currentMinutes)
            drawMinuteHand(this, minuteHandPaint, currentMinutes)
            drawSecondHand(this, secondHandPaint, currentSeconds)
            drawCenterDot(this, centerDotPaint, watchfacePaint.strokeWidth)
        }
        invalidate()
    }

    private fun resolveDefaultSize(widthSpec: Int, heightSpec: Int): Int {
        val width = MeasureSpec.getSize(widthSpec)
        val height = MeasureSpec.getSize(heightSpec)
        return if (width < height) {
            width
        } else height
    }

    private fun updateDimensions() {
        cX = width / 2f
        cY = height / 2f
        hourHandLength = width / 4f
        minuteHandLength = width / 3f
        secondHandLength = width / 2f
        watchfaceRadius = width / 2f - watchfacePaint.strokeWidth * 2
    }

    private fun updateTime() {
        val calendar = Calendar.getInstance()
        currentSeconds = calendar.get(Calendar.SECOND)
        currentMinutes = calendar.get(Calendar.MINUTE)
        currentHour = calendar.get(Calendar.HOUR_OF_DAY)
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
     * Draws a watchface without strokes
     */
    private fun drawWatchface(canvas: Canvas, watchfacePaint: Paint, rad: Float) {
        canvas.drawCircle(0f, 0f, rad, watchfacePaint)
    }

    /**
     * Draws hour strokes on watchface
     */
    private fun drawStrokes(canvas: Canvas, paint: Paint, startY: Float) {
        val endY = startY - (paint.strokeWidth * 3)
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
            this.drawLine(0f, 0f, 0f, watchfaceRadius / 2f, paint)
        }
    }

    /**
     * Draws minute hand by minutes given
     */
    private fun drawMinuteHand(canvas: Canvas, paint: Paint, minutes: Int) {
        canvas.withSave {
            this.rotate(minutes * -6f)
            this.drawLine(0f, 0f, 0f, watchfaceRadius / 1.5f, paint)
        }
    }

    /**
     * Draws second hand by seconds given
     */
    private fun drawSecondHand(canvas: Canvas, paint: Paint, seconds: Int) {
        canvas.withSave {
            this.rotate(seconds * -6f)
            this.drawLine(0f, 0f, 0f, watchfaceRadius / 1.2f, paint)
        }
    }

    /**
     * Draws center dot
     */
    private fun drawCenterDot(canvas: Canvas, centerDotPaint: Paint, rad: Float) {
        canvas.drawCircle(0f, 0f, rad, centerDotPaint)
    }
}
