package com.lithium.kotlin.dictionary.presentation.dictionary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.ListItemWordBinding
import com.lithium.kotlin.dictionary.domain.models.Word


class DictionaryAdapter(
    private val context: Context,
    private val callBacks: DictionaryFragment.CallBacks?,
    private val layoutInflater: LayoutInflater
){

    inner class WordHolder(private val binding: ListItemWordBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.viewModel = WordViewModel(context = context)
        }

        fun bind(word: Word){
            binding.apply {
                viewModel?.word = word

                wordLayout.wordEditText.setText(viewModel?.sequence)

                wordLayout.translationsEllipticalListView.items = viewModel?.translation?: listOf()
                wordLayout.categoriesEllipticalListView.items = viewModel?.categories?: listOf()

                viewModel?.loadIcon(wordLayout.wordIcon, word.photoFilePath)

                wordLayout.setOnClickListener{
                   callBacks?.onWordClicked(word.id)
                }
            }
        }
    }

    inner class Adapter(val words: List<Word>) : RecyclerView.Adapter<WordHolder>(){
        override fun getItemCount(): Int = words.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
            val binding = DataBindingUtil.inflate<ListItemWordBinding>(
                layoutInflater,
                R.layout.list_item_word,
                parent,
                false
            )
            return WordHolder(binding)
        }

        override fun onBindViewHolder(holder: WordHolder, position: Int) {
            val word = words[position]
            holder.bind(word)
        }

        override fun getItemId(position: Int): Long {
            return (words[position].id.mostSignificantBits.and(Long.MAX_VALUE))
        }
    }

}
