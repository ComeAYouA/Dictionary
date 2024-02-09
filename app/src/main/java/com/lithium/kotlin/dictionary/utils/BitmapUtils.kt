package com.lithium.kotlin.dictionary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.lithium.kotlin.dictionary.R

object BitmapUtils {

    private var editPictureBitmap: Bitmap? = null
    private var emptyPictureBitmap: Bitmap? = null
    fun getEditPictureBitmap(context: Context): Bitmap{

        return if (editPictureBitmap == null){
            var bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_edit_picture___)
            bitmap = Bitmap.createScaledBitmap(bitmap!!, 60, 60, false)

            editPictureBitmap = bitmap

            bitmap
        } else{
            editPictureBitmap!!
        }
    }

    fun getEmptyPictureBitmap(context: Context): Bitmap{

        return if (emptyPictureBitmap == null){
            var bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_empty_picture)
            bitmap = Bitmap.createScaledBitmap(bitmap!!, 350, 350, false)

            emptyPictureBitmap = bitmap

            bitmap
        } else{
            emptyPictureBitmap!!
        }

    }
}