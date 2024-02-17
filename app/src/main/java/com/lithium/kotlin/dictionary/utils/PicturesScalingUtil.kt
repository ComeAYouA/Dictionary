package com.lithium.kotlin.dictionary.utils

import android.util.Log
import android.util.Size
import java.lang.Float.min

object PicturesScalingUtil {


    fun getSizeThatFitIntoLayout(
        pictureWidth: Int,
        pictureHeight: Int,
        layoutWidth: Int,
        layoutHeight: Int
    ): Size{
        val widthPercentage = getWidthScalePercentage(pictureWidth, layoutWidth)
        val heightPercentage = getHeightScalePercentage(pictureHeight, layoutHeight)


        return when{
            widthPercentage > 1 && heightPercentage < 1 ->
                scaling(
                pictureWidth,
                pictureHeight,
                widthPercentage
            )
            widthPercentage < 1 && heightPercentage > 1 ->
                scaling(
                pictureWidth,
                pictureHeight,
                heightPercentage
            )
            else ->
                scaling(
                pictureWidth,
                pictureHeight,
                min(heightPercentage, widthPercentage)
            )

        }
    }

    // x * 0.8 = y
    private fun scaling(
        pictureWidth: Int,
        pictureHeight: Int,
        scale: Float
    ): Size{
        val scaling = 1 / (scale / 100f)
        val width = (pictureWidth * scaling).toInt()
        val height = (pictureHeight * scaling).toInt()
        if (width == 0 && height == 0) return Size(100, 100)
        return Size(width, height)
    }

    private fun getWidthScalePercentage(
        pictureWidth: Int,
        layoutWidth: Int,
    ) = (pictureWidth / layoutWidth.toFloat()) * 100
    private fun getHeightScalePercentage(
        pictureHeight: Int,
        layoutHeight: Int,
    ) = (pictureHeight / layoutHeight.toFloat()) * 100






}
