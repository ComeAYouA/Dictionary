package com.lithium.kotlin.dictionary.presentation.dictionary

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.domain.models.Word
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
    val translation: List<String>?
        get() = word?.translation?.toList()
    @get: Bindable
    val categories: List<String>?
        get() = word?.categories?.toList()

    @BindingAdapter("app:drawable")
    fun loadIcon(imageView: ImageView, path:String){
        if(word?.photoFilePath != ""){
            Picasso.with(context).load(File(path)).resize(170, 170).into(imageView)
        }else{
            Picasso.with(context).load(R.drawable.ic_empty_picture).resize(170, 170).into(imageView)
        }
    }
}