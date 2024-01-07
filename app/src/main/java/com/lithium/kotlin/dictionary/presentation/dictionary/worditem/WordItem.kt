package com.lithium.kotlin.dictionary.presentation.dictionary.worditem

import android.animation.FloatEvaluator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlin.math.exp

class WordItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ViewGroup(context, attrs, defStyleAttr) {
    val wordIcon : ImageButton
        get() = getChildAt(0) as ImageButton
    val wordEditText : EditText
        get () = getChildAt(1) as EditText
    val translationsText : TextView
        get() = getChildAt(2) as TextView
    val categoriesText : TextView
        get() = getChildAt(3) as TextView
    val deleteButton: Button
        get() = getChildAt(4) as Button

    private var expanded = false

    private var space  = 0

    private var animatedSpace = 0

    private val animator: ValueAnimator by lazy{
        Log.d("myTag", "animator")
        ValueAnimator
                .ofObject(FloatEvaluator(), 0, space)
                .apply {
                    duration = 100L
                    addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue.toString().toFloat().toInt()

                        if (expanded) {
                            animatedSpace = animatedValue
                        } else {
                            animatedSpace = space - animatedValue
                        }
                        requestLayout()
                    }
                }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        wordIcon.measure(
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec)/6, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec)/6, MeasureSpec.EXACTLY)
        )

        val wordEditTextWidth = MeasureSpec.getSize(widthMeasureSpec) - (wordIcon.measuredWidth + 16 + 50 + 50)
        val wordEditTextHeight = (MeasureSpec.getSize(heightMeasureSpec) - 10) / 3


        wordEditText.measure(
            MeasureSpec.makeMeasureSpec(wordEditTextWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(wordEditTextHeight, MeasureSpec.AT_MOST)
        )

        val translationsTextViewHeight = (MeasureSpec.getSize(heightMeasureSpec) - 10) / 3
        val translationsTextViewWidth = wordEditText.measuredWidth

        translationsText.measure(
            MeasureSpec.makeMeasureSpec(translationsTextViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(translationsTextViewHeight, MeasureSpec.AT_MOST)
        )

        val categoriesTextViewHeight = (MeasureSpec.getSize(heightMeasureSpec) - 10) / 3
        val categoriesTextViewWidth = wordEditText.measuredWidth

        categoriesText.measure(
            MeasureSpec.makeMeasureSpec(categoriesTextViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(categoriesTextViewHeight, MeasureSpec.AT_MOST)
        )

        deleteButton.measure(
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(180, MeasureSpec.EXACTLY)
        )

        val viewHeight = 30 + wordEditText.measuredHeight + 20 + translationsText.measuredHeight + 20 + animatedSpace
        space = categoriesText.measuredHeight + 40 + deleteButton.measuredHeight

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.AT_MOST)
        )
    }
    override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {

        wordIcon.layout(
            26,
            26,
            wordIcon.measuredWidth + 16 ,
            wordIcon.measuredHeight
        )

        wordEditText.layout(
            16 + wordIcon.measuredWidth + 50,
            20,
            r - 50,
            wordEditText.measuredHeight + 20
        )

        translationsText.layout(
            16 + wordIcon.measuredWidth + 50,
            20 + wordEditText.measuredHeight  + 20,
            r - 50,
            20 + wordEditText.measuredHeight + 20 + translationsText.measuredHeight
        )

        categoriesText.layout(
            16 + wordIcon.measuredWidth + 50,
            20+ wordEditText.measuredHeight + 20 + translationsText.measuredHeight + 20,
            r - 50,
            20+ wordEditText.measuredHeight + 20 + translationsText.measuredHeight + 20 + categoriesText.measuredHeight
        )

        deleteButton.layout(
            15,
            20+ wordEditText.measuredHeight + 20 + translationsText.measuredHeight + 20 + categoriesText.measuredHeight + 40,
            r - 25,
            20+ wordEditText.measuredHeight + 20 + translationsText.measuredHeight + 20 + categoriesText.measuredHeight + 40 + deleteButton.measuredHeight
        )

    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return !expanded
    }
    override fun performClick(): Boolean {
        expanded = !expanded
        animator.start()
        Log.d("myTag", "click on $space")
        return super.performClick()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onDetachedFromWindow() {
        expanded = false
        animatedSpace = 0

        super.onDetachedFromWindow()
    }



}
