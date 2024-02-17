package com.lithium.kotlin.dictionary.utils

import android.widget.ImageView
import com.lithium.kotlin.dictionary.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.File

object WordIconUtil {
    fun loadCorrectWordIcon(imagePath: String, view: ImageView): RequestCreator =
        Picasso.with(view.context).run {
            if (imagePath != ""){
                load(File(imagePath))
            } else {
                load(R.drawable.ic_empty_picture)
            }
        }
}