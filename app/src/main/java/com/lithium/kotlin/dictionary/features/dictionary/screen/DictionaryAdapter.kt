package com.lithium.kotlin.dictionary.features.dictionary.screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.ListItemWordBinding
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.utils.WordIconUtil

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


    fun setWords(data: List<Word>){
        val diffUtilCallback = DictionaryDiffUtilCallback(words, data)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)

        words.clear()
        words.addAll(data)

        diffUtilResult.dispatchUpdatesTo(this)
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

            WordIconUtil.loadCorrectWordIcon(
                imagePath = word.photoFilePath,
                view = binding.wordIcon
            ).resize(174, 174).into(wordIcon)

            wordLayout.isClickable = true

            gearIcon.setOnClickListener {
                onWordEditBlock(word)
            }
        }
    }
}