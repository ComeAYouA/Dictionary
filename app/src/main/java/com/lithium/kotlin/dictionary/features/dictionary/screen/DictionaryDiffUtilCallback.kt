package com.lithium.kotlin.dictionary.features.dictionary.screen

import androidx.recyclerview.widget.DiffUtil
import com.lithium.kotlin.dictionary.domain.models.Word

class DictionaryDiffUtilCallback(
    private val oldList: List<Word>,
    private val newList: List<Word>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size


    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].equals(newList[newItemPosition].id)
    }

}