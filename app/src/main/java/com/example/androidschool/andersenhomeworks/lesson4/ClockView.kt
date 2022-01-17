package com.example.androidschool.andersenhomeworks.lesson4

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.example.androidschool.andersenhomeworks.R
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): View(context, attrs, defStyleAttr, defStyleRes), CoroutineScope {

    companion object {
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

    private var watchfacePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var watchfaceCenterDotPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var clockRunningJob: Job? = null
    private var isRunning = false

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

            watchfaceCenterDotPaint.apply {
                strokeWidth = typedArray.getDimension(
                    R.styleable.ClockView_hourHand_thickness,
                    context.dpToPx(DEFAULT_DOT_THICKNESS)
                )
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            resolveDefaultSize(widthMeasureSpec, heightMeasureSpec),
            resolveDefaultSize(widthMeasureSpec, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f

        canvas?.apply {
            initWatchface(this, cx, cy)
            drawStrokes(this, watchfacePaint, watchfaceRadius)
            drawCircle(0f, 0f, watchfaceRadius, watchfacePaint)
            drawHourHand(this, hourHandPaint, currentHour, currentMinutes)
            drawMinuteHand(this, minuteHandPaint, currentMinutes)
            drawSecondHand(this, secondHandPaint, currentSeconds)
            drawCircle(0f, 0f, watchfaceThickness * 2, watchfaceCenterDotPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        handHourLength = width / 4f
        minuteHourLength = width / 3f
        secondHourLength = width / 2f
        watchfaceRadius = width / 2f - watchfaceThickness * 2
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancel()
    }

    /**
     * Sets hands in motion
     */
    fun start() {
        if (isRunning) return
        clockRunningJob = launch {
            while (true) {
                val calendar = Calendar.getInstance()
                setTime(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND)
                )
                yield()
            }
        }
        isRunning = true
    }

    /**
     * Stops hands
     */
    fun stop() {
        clockRunningJob?.cancel()
        clockRunningJob = null
        isRunning = false
    }

    /**
     * Sets time and force a view to draw
     */
    private fun setTime(hours: Int, minutes: Int, seconds: Int) {
        currentHour = hours
        currentMinutes = minutes
        currentSeconds = seconds
        invalidate()
    }

    private fun resolveDefaultSize(widthSpec: Int, heightSpec: Int): Int {
        val width = MeasureSpec.getSize(widthSpec)
        val height = MeasureSpec.getSize(heightSpec)
        return if (width < height) {
            width
        } else height
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
}

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}