package com.lithium.kotlin.dictionary.utils

import android.content.res.Resources
import android.graphics.Rect
import androidx.fragment.app.DialogFragment


object DialogWindowUtil{
    fun DialogFragment.setLayoutPercents(widthPercentage: Int, heightPercentage: Int) {
        val wPercent = widthPercentage.toFloat() / 100
        val hPercent = heightPercentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * wPercent
        val percentHeight = rect.height() * hPercent
        dialog?.window?.setLayout(percentWidth.toInt(), percentHeight.toInt())
    }
}