package com.lithium.kotlin.dictionary.common.ui.customviews

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

class WordItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ViewGroup(context, attrs, defStyleAttr) {
    val wordIcon : ImageButton
        get() = getChildAt(0) as ImageButton
    val wordEditText : TextView
        get () = getChildAt(1) as TextView
    val translationsEllipticalListView : EllipticalListView
        get() = getChildAt(2) as EllipticalListView
    val categoriesEllipticalListView : EllipticalListView
        get() = getChildAt(3) as EllipticalListView
    val gearIcon : ImageButton
        get() = getChildAt(4) as ImageButton


    private var expanded = false
    private var expandSpace  = 0
    private var animatedExpandedSpace = 0

    private var verticalItemsSpace = 26
    private var horizontalItemsSpace = 50

    private val animator: ValueAnimator
        get() {
        return ValueAnimator
                .ofObject(FloatEvaluator(), 0, expandSpace)
                .apply {
                    duration = 130L
                    addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue.toString().toFloat().toInt()

                        if (!expanded) {
                            animatedExpandedSpace = expandSpace - animatedValue
                        } else {
                            animatedExpandedSpace = animatedValue
                        }
                        requestLayout()
                    }
                }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val wordIconSize = 150

        wordIcon.measure(
            MeasureSpec.makeMeasureSpec(wordIconSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(wordIconSize, MeasureSpec.EXACTLY)
        )


        val wordEditTextWidth = MeasureSpec.getSize(widthMeasureSpec) - (wordIcon.measuredWidth + horizontalItemsSpace/2 + horizontalItemsSpace * 2)
        val verticalHConstraints = (MeasureSpec.getSize(heightMeasureSpec) - 10) / 3


        wordEditText.measure(
            MeasureSpec.makeMeasureSpec(wordEditTextWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(verticalHConstraints, MeasureSpec.AT_MOST)
        )

        translationsEllipticalListView.measure(
            MeasureSpec.makeMeasureSpec(wordEditText.measuredWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(verticalHConstraints, MeasureSpec.AT_MOST)
        )

        categoriesEllipticalListView.measure(
            MeasureSpec.makeMeasureSpec(wordEditText.measuredWidth - gearIcon.measuredWidth - verticalItemsSpace, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(verticalHConstraints, MeasureSpec.AT_MOST)
        )

        gearIcon.measure(
            MeasureSpec.makeMeasureSpec(64, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec( 64, MeasureSpec.EXACTLY)
        )


        val viewHeight = verticalItemsSpace + wordEditText.measuredHeight + verticalItemsSpace + translationsEllipticalListView.measuredHeight + verticalItemsSpace + animatedExpandedSpace
        expandSpace = categoriesEllipticalListView.measuredHeight + verticalItemsSpace

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.AT_MOST)
        )
    }
    override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {
        wordIcon.layout(
            horizontalItemsSpace / 2,
            verticalItemsSpace,
            wordIcon.measuredWidth + horizontalItemsSpace / 2 ,
            wordIcon.measuredHeight + verticalItemsSpace
        )

        val leftMargin = horizontalItemsSpace / 2 + wordIcon.measuredWidth + horizontalItemsSpace

        wordEditText.layout(
            leftMargin,
            verticalItemsSpace,
            wordEditText.measuredWidth + leftMargin,
            wordEditText.measuredHeight + verticalItemsSpace
        )

        val translationBottom = verticalItemsSpace * 2 + wordEditText.measuredHeight
        val translationTop = translationBottom + translationsEllipticalListView.measuredHeight

        translationsEllipticalListView.layout(
            leftMargin,
            translationBottom,
            leftMargin + translationsEllipticalListView.measuredWidth,
            translationTop
        )


        val categoriesBottom = translationTop + verticalItemsSpace
        val categoriesTop = categoriesBottom + categoriesEllipticalListView.measuredHeight

        categoriesEllipticalListView.layout(
            leftMargin,
            categoriesBottom,
            leftMargin + categoriesEllipticalListView.measuredWidth,
            categoriesTop
        )

        gearIcon.layout(
            leftMargin + categoriesEllipticalListView.measuredWidth + horizontalItemsSpace,
            categoriesTop - gearIcon.measuredHeight,
            leftMargin + categoriesEllipticalListView.measuredWidth + gearIcon.measuredWidth + horizontalItemsSpace,
            categoriesTop
        )
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return !expanded
    }
    override fun performClick(): Boolean {
        expanded = !expanded
        animator.start()

        return super.performClick()
    }

    override fun onDetachedFromWindow() {
        expanded = false
        animatedExpandedSpace = 0

        super.onDetachedFromWindow()
    }
}
