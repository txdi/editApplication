package com.chensi.editapplication.item.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.blue
import com.chensi.editapplication.R

class MyCustomView : View {

    private var midText: String? = null

    private val defaultMinWidth = 50
    private val defaultMinHeight = 50

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = Color.RED
        this.style = Paint.Style.FILL_AND_STROKE
        this.strokeWidth = 10F
    }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = Color.RED
        this.style = Paint.Style.STROKE
        this.strokeWidth = 10F
    }

    constructor(
        context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attributeSet, defStyleAttr, defStyleRes) {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.MyCustomView)
        midText = ta.getString(R.styleable.MyCustomView_midText)
        ta.recycle()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : this(
        context, attributeSet, defStyleAttr, 0
    )

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context, attributeSet, 0
    )

    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            val resultWidth = widthSize.coerceAtMost(defaultMinWidth)
            val resultHeight = heightSize.coerceAtMost(defaultMinHeight)
            setMeasuredDimension(resultWidth, resultHeight)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        //绘制边框
        canvas?.drawLine(0F, 0F, measuredWidth.toFloat(), 0F, paint)
        canvas?.drawLine(0F, 0F, 0F, measuredHeight.toFloat(), paint)
        canvas?.drawLine(
            measuredWidth.toFloat(), 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), paint
        )
        canvas?.drawLine(
            0F, measuredHeight.toFloat(), measuredWidth.toFloat(), measuredHeight.toFloat(), paint
        )

        val radius = measuredHeight.coerceAtMost(measuredHeight) / 3

        //绘制圆角
        canvas?.drawArc(
            0F, 0F, radius.toFloat(), radius.toFloat(), 180F, 90F, false, arcPaint
        )

        canvas?.drawArc(
            (measuredWidth - radius).toFloat(),
            0F,
            measuredWidth.toFloat(),
            radius.toFloat(),
            270F,
            90F,
            false,
            arcPaint
        )

        canvas?.drawArc(
            0F,
            (measuredHeight - radius).toFloat(),
            radius.toFloat(),
            measuredHeight.toFloat(),
            90F,
            90F,
            false,
            arcPaint
        )

        canvas?.drawArc(
            (measuredWidth - radius).toFloat(),
            (measuredHeight - radius).toFloat(),
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            0F,
            90F,
            false,
            arcPaint
        )

    }


}