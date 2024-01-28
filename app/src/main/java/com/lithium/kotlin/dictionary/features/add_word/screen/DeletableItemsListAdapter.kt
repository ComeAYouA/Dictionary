package com.lithium.kotlin.dictionary.features.add_word.screen

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R


class DeletableItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(translation: String) {
        val translationTextView = view.findViewById(R.id.translation_text) as TextView
        translationTextView.text = translation

        val deleteButton = view.findViewById(R.id.delete_button) as Button

        deleteButton.setOnClickListener {
            (bindingAdapter as DeletableItemAdapter).apply {
                deletableItemsList.remove(translation)
                notifyItemRemoved(position)
            }
        }

    }
}

class DeletableItemAdapter :
    RecyclerView.Adapter<DeletableItemHolder>() {

    val deletableItemsList: MutableSet<String> = mutableSetOf()

    override fun getItemCount(): Int = deletableItemsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletableItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.list_item_deletable,
                parent,
                false
            )
        return DeletableItemHolder(view)
    }

    override fun onBindViewHolder(holder: DeletableItemHolder, position: Int) {
        val word = deletableItemsList.toList()[position]
        holder.bind(word)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDeletableItemsList(data: MutableSet<String>) {
        if (data == mutableSetOf("")) return
        deletableItemsList.clear()
        deletableItemsList.addAll(data)
        notifyDataSetChanged()
    }

    fun addCellToList(input: String){
        if (!deletableItemsList.contains(input) && input != ""){
            deletableItemsList.add(input)
            notifyItemInserted(deletableItemsList.size - 1)
        }
    }
}