package com.lithium.kotlin.dictionary.presentation.dictionary.screen.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.DKGRAY
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.presentation.dictionary.utils.BitmapUtils
import java.lang.Integer.max

class EllipticalListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    var items : List<String> = listOf()
        set(value){
            field = value
            requestLayout()
        }

    var expanded = false
        set(value) {
            field = value
            requestLayout()
        }

    private var itemsMeasurement = listOf<Pair<Int, Int>>()
    private var itemsLayout = listOf<Pair<Float, Float>>()
    private var bounds = Rect()

    private var backColor = 0


    private var tSize = 42f

    private val itemsPadding = 32
    private var maxH = 40
    private var internalPadding = 8

    private val textPaint = Paint().apply{
        isAntiAlias = true
        color = Color.LTGRAY
        textSize = tSize
    }
    private val backPaint = Paint().apply {
        isAntiAlias = true
        color = backColor
    }
    private val imagePaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
    }
    
    private var bitmap: Bitmap? = null

    init {
        context.applicationContext.applicationContext.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.EllipticalListView,
            defStyleAttr = defStyleAttr
        ) {
            backColor = getColor(R.styleable.EllipticalListView_backColor, DKGRAY)
            backPaint.color = backColor
        }
        bitmap = BitmapUtils.getEditPictureBitmap(context)
    }


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var countOfLines = 1
        var currentLineLength = 0
        var maxWidth = 0

        val specWidth = MeasureSpec.getSize(widthMeasureSpec)

        fun outOfBoundsCheck(){
            if (currentLineLength > specWidth){
                countOfLines ++
                currentLineLength = bounds.width() + internalPadding * 2
            }
        }

        itemsMeasurement = items.mapIndexed { idx, item ->
            textPaint.getTextBounds(item, 0, item.length, bounds)

            currentLineLength += bounds.width() + internalPadding * 2

            outOfBoundsCheck()

            maxWidth = max(maxWidth, currentLineLength)

            if (idx != items.lastIndex) currentLineLength += itemsPadding

            val bWidth = bounds.width()
            val bHeight = bounds.height()

            maxH = max(maxH, bHeight)

            Pair(bWidth, bHeight)
        }


        maxWidth = max(maxWidth, currentLineLength)

        if (expanded) {
            currentLineLength += bitmap!!.width + 20 + itemsPadding

            bounds.set(
                Rect(0,
                    bitmap!!.width + 20 + itemsPadding,
                    0, 0)
            )

            outOfBoundsCheck()

            maxWidth = max(maxWidth, currentLineLength)
        }

        val viewHeight = (maxH + internalPadding * 3) * countOfLines + itemsPadding / 2 * (countOfLines - 1)

        setMeasuredDimension(
            MeasureSpec.getSize(maxWidth),
            MeasureSpec.getSize(viewHeight)
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        var startX = internalPadding.toFloat()
        var startY = maxH + internalPadding.toFloat()

        fun outOfBoundsCheck(idx: Int){
            if (startX + itemsMeasurement[idx + 1].first + internalPadding > this.measuredWidth){
                startX = internalPadding.toFloat()
                startY += maxH + itemsPadding/2 + internalPadding * 3
            }
        }

        itemsLayout = items.mapIndexed { idx, str ->
            val resXY = Pair(startX, startY)

            startX += itemsMeasurement[idx].first + itemsPadding

            if (idx != items.lastIndex){
                outOfBoundsCheck(idx)
            }

            resXY
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawItems(canvas)
        drawEditButton(canvas)
    }

    private fun drawItems(canvas: Canvas){
        itemsLayout.forEachIndexed { idx, XY ->

            canvas.drawRoundRect(
                XY.first - internalPadding,
                XY.second - maxH - internalPadding,
                XY.first + itemsMeasurement[idx].first.toFloat() + internalPadding,
                XY.second + internalPadding * 2,
                10f, 10f,
                backPaint
            )

            canvas.drawText(
                items[idx],
                0,
                items[idx].length,
                XY.first,
                XY.second,
                textPaint
            )
        }
    }

    private fun drawEditButton(canvas: Canvas){
        var startX: Float

        if (itemsLayout.isEmpty()){
            startX = internalPadding.toFloat()
        } else{
            startX = itemsLayout.last().first
            startX += itemsMeasurement.last().first + itemsPadding
        }

        var startY = if (itemsLayout.isEmpty()){
            maxH + internalPadding.toFloat()
        } else{
            itemsLayout.last().second
        }.toFloat()


        if (startX + bitmap!!.width > this.measuredWidth){
            startX = internalPadding.toFloat()
            startY += maxH + 20f + internalPadding * 3
        }

        if (expanded){
            canvas.drawBitmap(bitmap!!, startX, startY - bitmap!!.height + internalPadding.toFloat() * 2,  imagePaint)
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        val toast = Toast.makeText(context, "Click", Toast.LENGTH_SHORT)
        toast.show()
        return super.performClick()
    }
}

