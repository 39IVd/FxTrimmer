package com.fx.fxtrimmer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.fx.fxtrimmer.R
import com.fx.fxtrimmer.interfaces.OnProgressVideoListener
import com.fx.fxtrimmer.interfaces.OnRangeSeekBarListener

class ProgressBarView @JvmOverloads constructor(
    @NonNull context: Context, attrs: AttributeSet,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr), OnRangeSeekBarListener, OnProgressVideoListener {

    private var mProgressHeight: Int = 0
    private var mViewWidth: Int = 0

    private val mBackgroundColor = Paint()
    private val mProgressColor = Paint()

    private var mBackgroundRect: Rect? = null
    private var mProgressRect: Rect? = null

    init {
        init()
    }

    private fun init() {
        val lineBackground = ContextCompat.getColor(context, R.color.watermelon)

        mProgressHeight =
            context.resources.getDimensionPixelOffset(R.dimen.progress_video_line_height)

        mBackgroundColor.isAntiAlias = true
        mBackgroundColor.color = lineBackground
        mBackgroundColor.strokeWidth = 30f
        mBackgroundColor.style = Paint.Style.STROKE

        mProgressColor.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val minW = paddingLeft + paddingRight + suggestedMinimumWidth
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mViewWidth = View.resolveSizeAndState(minW, widthMeasureSpec, 1)
        }

        val minH = paddingBottom + paddingTop + mProgressHeight
        var viewHeight = 0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            viewHeight = View.resolveSizeAndState(minH, heightMeasureSpec, 1)
        }

        setMeasuredDimension(mViewWidth, viewHeight)
    }

    override fun onDraw(@NonNull canvas: Canvas) {
        super.onDraw(canvas)

        drawLineBackground(canvas)
    }

    private fun drawLineBackground(@NonNull canvas: Canvas) {
        if (mBackgroundRect != null) {
            canvas.drawRect(mBackgroundRect!!, mBackgroundColor)
        }
    }

    override fun onCreate(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
        updateBackgroundRect(index, value)
    }

    override fun onSeek(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
        updateBackgroundRect(index, value)
    }

    override fun onSeekStart(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
        updateBackgroundRect(index, value)
    }

    override fun onSeekStop(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
        updateBackgroundRect(index, value)
    }

    private fun updateBackgroundRect(index: Int, value: Float) {

        if (mBackgroundRect == null) {
            mBackgroundRect = Rect(0, 0, mViewWidth, mProgressHeight)
        }

        val newValue = (mViewWidth * value / 100).toInt()
        if (index == 0) {
            mBackgroundRect = Rect(
                newValue,
                mBackgroundRect!!.top,
                mBackgroundRect!!.right,
                mBackgroundRect!!.bottom
            )
        } else {
            mBackgroundRect = Rect(
                mBackgroundRect!!.left,
                mBackgroundRect!!.top,
                newValue,
                mBackgroundRect!!.bottom
            )
        }

        updateProgress(0, 0, 0.0f)
    }

    override fun updateProgress(time: Int, max: Int, scale: Float) {

        if (scale == 0f) {
            mProgressRect = Rect(0, mBackgroundRect!!.top, 0, mBackgroundRect!!.bottom)
        } else {
            val newValue = (mViewWidth * scale / 100).toInt()
            mProgressRect = Rect(
                mBackgroundRect!!.left,
                mBackgroundRect!!.top,
                newValue,
                mBackgroundRect!!.bottom
            )
        }

        invalidate()
    }
}
