package com.lithium.kotlin.dictionary.presentation.word

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R

class EditWordAdapter(
    private val layoutInflater: LayoutInflater
) {
    inner class DeletableItemHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(translation: String){
            val translationTextView = view.findViewById(R.id.translation_text) as TextView
            translationTextView.text = translation

            val deleteButton = view.findViewById(R.id.delete_button) as Button
            deleteButton.setOnClickListener {
                (bindingAdapter as DeletableItemAdapter).apply {
                    data.remove(translation)
                    notifyItemRemoved(position)
                }
            }

        }
    }
    inner class DeletableItemAdapter(val data: MutableSet<String>): RecyclerView.Adapter<DeletableItemHolder>(){
        override fun getItemCount(): Int = data.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletableItemHolder {
            val view = layoutInflater.inflate(R.layout.list_item_deletable, parent, false)
            return DeletableItemHolder(view)
        }

        override fun onBindViewHolder(holder: DeletableItemHolder, position: Int) {
            val word = data.toList()[position]
            holder.bind(word)
        }
    }
}