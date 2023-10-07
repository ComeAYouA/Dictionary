package com.lithium.kotlin.dictionary.view_models

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.Word
import com.squareup.picasso.Picasso
import java.io.File

class WordViewModel(private val context: Context): BaseObservable() {
    var word: Word? = null
        set(word) {
            field = word
            notifyChange()
        }
    @get: Bindable
    val sequence: String?
        get() = word?.sequence

    @get: Bindable
    val translation: String
        get() = word?.translation.toString()

    @BindingAdapter("app:drawable")
    fun loadIcon(imageView: ImageView, path:String){
        if(word?.photoFilePath != ""){
            Picasso.with(context).load(File(path)).into(imageView)
        }else{
            Picasso.with(context).load(R.drawable.ic_empty_picture).into(imageView)
        }
    }
}