package com.lithium.kotlin.dictionary.features.dictionary.screen.dictionaryRV.item_word

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.DKGRAY
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.features.dictionary.utils.BitmapUtils
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

    private var bounds = Rect()
    private var itemsMeasurement = listOf<Pair<Int, Int>>()
    private var itemsLayout = listOf<Pair<Float, Float>>()

    private var backColor = 0

    private var tSize = 42f

    private val itemsSpace = 32
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

            if (idx != items.lastIndex) currentLineLength += itemsSpace

            val boundsWidth = bounds.width()
            val boundsHeight = bounds.height()

            maxH = max(maxH, boundsHeight)

            Pair(boundsWidth, boundsHeight)
        }

        val viewHeight = (maxH + internalPadding * 3) * countOfLines + itemsSpace / 2 * (countOfLines - 1)

        setMeasuredDimension(
            widthMeasureSpec,
            MeasureSpec.getSize(viewHeight)
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        var startX = internalPadding.toFloat()
        var startY = maxH + internalPadding.toFloat()

        fun outOfBoundsCheck(idx: Int){
            if (startX + itemsSpace + itemsMeasurement[idx + 1].first > this.measuredWidth){
                startX = internalPadding.toFloat()
                startY += maxH + itemsSpace/2 + internalPadding * 3
            }
        }

        itemsLayout = items.mapIndexed { idx, str ->
            val resXY = Pair(startX, startY)

            startX += itemsMeasurement[idx].first + itemsSpace

            if (idx != items.lastIndex){
                outOfBoundsCheck(idx)
            }

            resXY
        }
    }

    override fun onDraw(canvas: Canvas) {
        itemsLayout.forEachIndexed { idx, xy ->

            drawTextBackground(canvas, xy, idx)

            drawText(canvas, xy, idx)
        }
    }

    private fun drawTextBackground(canvas: Canvas, xy: Pair<Float, Float>, textIdx: Int){
        canvas.drawRoundRect(
            xy.first - internalPadding,
            xy.second - maxH - internalPadding,
            xy.first + itemsMeasurement[textIdx].first.toFloat() + internalPadding,
            xy.second + internalPadding * 2,
            10f, 10f,
            backPaint
        )
    }

    private fun drawText(canvas: Canvas, xy: Pair<Float, Float>, textIdx: Int){
        canvas.drawText(
            items[textIdx],
            0,
            items[textIdx].length,
            xy.first,
            xy.second,
            textPaint
        )
    }
}

