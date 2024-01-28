package com.lithium.kotlin.dictionary.features.dictionary.screen.dictionaryRV

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.ListItemWordBinding
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.features.dictionary.screen.DictionaryFragment
import com.lithium.kotlin.dictionary.features.edit_word.screen.EditWordDialog
import com.squareup.picasso.Picasso
import java.io.File
import java.util.UUID
import javax.inject.Inject

private const val NO_TRANSLATION = "Нет перевода"
private const val NO_CATEGORIES = "Нет категорий"

class DictionaryAdapter: RecyclerView.Adapter<WordHolder>(){

    val words: MutableList<Word> = mutableListOf()

    private var onWordEditBlock: (Word) -> Unit = {}

    fun setWordEditClickListener(block: (Word) -> Unit) {
        onWordEditBlock = block
    }
    override fun getItemCount(): Int = words.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val binding = DataBindingUtil.inflate<ListItemWordBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_word,
            parent,
            false
        )
        return WordHolder(binding)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        val word = words[position]
        holder.bind(word, onWordEditBlock)
    }

    override fun getItemId(position: Int): Long {
        return (words[position].id.mostSignificantBits.and(Long.MAX_VALUE))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setWords(data: List<Word>){
        words.clear()
        words.addAll(data)
        notifyDataSetChanged()
    }
}

class WordHolder (private val binding: ListItemWordBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(word: Word, onWordEditBlock: (Word) -> Unit){
        binding.apply {
            wordTextView.text = word.sequence

            translationsEllipticalView.items =
                word.translation.toList().ifEmpty { listOf(NO_TRANSLATION) }

            categoriesEllipticalView.items =
                word.categories.toList().ifEmpty { listOf(NO_CATEGORIES) }

            if(word.photoFilePath != ""){
                Picasso.with(wordIcon.context)
                    .load(File(word.photoFilePath))
                    .resize(170, 170)
                    .into(wordIcon)
            }else{
                Picasso.with(wordIcon.context)
                    .load(R.drawable.ic_empty_picture)
                    .resize(170, 170)
                    .into(wordIcon)
            }

            wordLayout.isClickable = true

            gearIcon.setOnClickListener {
                onWordEditBlock(word)
            }
        }
    }
}