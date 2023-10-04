package com.lithium.kotlin.dictionary

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

class EditDictionaryViewModel(){
    private val repository: WordsRepository = WordsRepository.get()
    private val wordIdLiveData = MutableLiveData<UUID>()

    val wordLiveData : LiveData<Word?> =
        Transformations.switchMap(wordIdLiveData){ wordId ->
            repository.getWord(wordId)
        }
    fun loadWord(wordId: UUID){
        wordIdLiveData.value = wordId
    }
    fun saveWord(word: Word){
        repository.updateWord(word)
    }
    fun addWord(word: Word){
        repository.addWord(word)
    }
}