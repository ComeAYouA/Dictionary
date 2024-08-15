package com.lithium.kotlin.dictionary.features.add_word.screen

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lithium.kotlin.dictionary.R
import javax.inject.Inject


class DeletableItemAdapter:
    RecyclerView.Adapter<DeletableItemHolder>()
{

    interface OnDeleteListener{
        fun onItemDeleted(item: String)
    }

    var onDeleteListener: OnDeleteListener? = null

    val deletableItemsList: HashSet<String> = HashSet()

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
        val item = deletableItemsList.toList()[position]
        holder.bind(
            item
        ) {
            onDeleteListener?.onItemDeleted(item)
            this.notifyItemRemoved(position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setDeletableItemsList(data: MutableSet<String>) {
        deletableItemsList.clear()
        deletableItemsList.addAll(data)
        notifyDataSetChanged()
    }
}

class DeletableItemHolder(view: View) : ViewHolder(view) {

    private val translationTextView: TextView = view.findViewById(R.id.translation_text)
    private val deleteButton: ImageButton = view.findViewById(R.id.delete_button)

    fun bind(translation: String, onDelete: () -> Unit) {
        translationTextView.text = translation

        deleteButton.setOnClickListener { onDelete() }
    }
}