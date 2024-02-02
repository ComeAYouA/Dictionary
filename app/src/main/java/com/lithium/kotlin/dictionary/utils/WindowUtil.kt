package com.lithium.kotlin.dictionary.utils

import android.content.res.Resources
import android.graphics.Rect

object WindowUtil {

    private val displayMetrics = Resources.getSystem().displayMetrics
    private val displayRect = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }

    fun getPixelFromPercentsWidth(percentage: Int) =
        displayRect.width() * percentage.toFloat() / 100

    fun getPixelFromPercentsHeight(percentage: Int) =
        displayRect.height() * percentage.toFloat() / 100

    fun getWindowWidth() = displayRect.width()
    fun getWindowHeight() = displayRect.height()

}