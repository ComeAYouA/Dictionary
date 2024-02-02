package com.lithium.kotlin.dictionary.features.educational_cards.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PathIterator
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.lithium.kotlin.dictionary.domain.models.Coordinates
import com.lithium.kotlin.dictionary.utils.WindowUtil
import java.lang.Integer.max

class EducationalCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private var cardWidth = WindowUtil.getPixelFromPercentsWidth(80)
    private var cardHeight = WindowUtil.getPixelFromPercentsHeight(64)

    var wordText = "LONGLONGLONGLONG"

    var wordTranslate = mutableListOf("много", "слов", "для", "проверки", "текса")
    private val wordTranslationStringsList = mutableListOf<Pair<Coordinates, String>>()
    private val wordTranslateWidth = cardWidth * 0.9f
    private val wordTranslateHeight = cardHeight * 0.4f
    private val textBounds = Rect()
    private val supportStringBuilder = StringBuilder()
    private var maxHeightOfText = 0

    var iconPath: String? = null


    private var backColor = Color.DKGRAY
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.LTGRAY
        textSize = 72f
    }
    private var acceptBackgroundOvalRect = Rect().apply {
        top = WindowUtil.getWindowHeight()
        right = WindowUtil.getWindowWidth()
    }


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var currentLineLength = 0

        supportStringBuilder.clear()
        wordTranslationStringsList.clear()

        Log.d("myTag", "measure")

        wordTranslate.forEach{ translationStr ->
            textPaint.getTextBounds(translationStr, 0, translationStr.length, textBounds)

            currentLineLength += textBounds.width()

            if (currentLineLength >= wordTranslateWidth){
                wordTranslationStringsList.add(
                    Pair(
                        Coordinates(),
                        supportStringBuilder.toString()
                    )
                )

                Log.d("myTag", translationStr)

                currentLineLength = textBounds.width()

                supportStringBuilder.clear()
            }

            supportStringBuilder.append("$translationStr, ")

            maxHeightOfText = max(maxHeightOfText, textBounds.height())
        }

        if (supportStringBuilder.isNotEmpty()) wordTranslationStringsList.add(
            Pair(
                Coordinates(),
                supportStringBuilder.toString()
            )
        )

        setMeasuredDimension(
            widthMeasureSpec,
            heightMeasureSpec
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        Log.d("myTag", "layout")

        var startY = maxHeightOfText

        wordTranslationStringsList.forEach {
            it.first.yCoordinate = startY.toFloat()
            startY += maxHeightOfText + 20
        }
    }

    override fun onDraw(canvas: Canvas) {

        val windowWidth = WindowUtil.getWindowWidth()
        val windowHeight = WindowUtil.getWindowHeight()

        canvas.drawOval(
            -windowWidth + 400f,
            WindowUtil.getPixelFromPercentsHeight(14),
            400f,
            WindowUtil.getPixelFromPercentsHeight(86),
            backgroundPaint.apply { color = Color.RED; alpha = 16 }
        )

        canvas.drawOval(
            windowWidth - 400f,
            WindowUtil.getPixelFromPercentsHeight(14),
            windowWidth * 2 - 400f,
            WindowUtil.getPixelFromPercentsHeight(86),
            backgroundPaint.apply { color = Color.GREEN; alpha = 16 }
        )


        canvas.drawRoundRect(
            (windowWidth - cardWidth) / 2,
            (windowHeight - cardHeight) / 2,
            (windowWidth - cardWidth) / 2 + cardWidth,
            (windowHeight - cardHeight) / 2 + cardHeight,
            80f, 90f,
            backgroundPaint.apply { color = Color.DKGRAY }
        )

        val wordTextBounds = Rect()
        textPaint.getTextBounds(wordText, 0, wordText.length, wordTextBounds)

        canvas.drawRoundRect(
            ((windowWidth - cardWidth) / 2 + cardWidth * 0.28f),
            ((windowHeight - cardHeight) / 2 + cardHeight * 0.06f),
            ((windowWidth - cardWidth) / 2 + cardWidth * 0.28f + cardWidth * 0.42f),
            ((windowHeight - cardHeight) / 2 + cardHeight * 0.30f),
            30f, 30f,
            backgroundPaint.apply { color = Color.LTGRAY }
        )

        canvas.drawText(
            wordText,
            0,
            wordText.length,
            (windowWidth - cardWidth) / 2 ,
            (windowHeight - cardHeight) / 2 + cardHeight * 0.40f,
            textPaint
        )

        wordTranslationStringsList.forEach {
            canvas.drawText(
                it.second,
                0,
                it.second.length,
                (windowWidth - cardWidth) / 2 + it.first.xCoordinate,
                (windowHeight - cardHeight) / 2 + cardHeight * 0.66f + it.first.yCoordinate,
                textPaint
            )
        }

        Log.d("myTag", wordTranslationStringsList.fold(""){ acc, cs ->
            acc + "x: ${cs.first.xCoordinate} y: ${cs.first.yCoordinate} ${cs.second}"
        }
        )

        super.onDraw(canvas)
    }
}