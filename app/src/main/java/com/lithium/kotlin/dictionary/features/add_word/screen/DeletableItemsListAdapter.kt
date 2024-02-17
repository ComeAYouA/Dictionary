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
    RecyclerView.Adapter<ViewHolder>() {

    enum class ViewType{
        DeletableItem, AddButton
    }

    val deletableItemsList: MutableSet<String> = mutableSetOf()

    override fun getItemCount(): Int = deletableItemsList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position in deletableItemsList.indices)
            ViewType.DeletableItem.ordinal
        else
            ViewType.AddButton.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType){
            ViewType.DeletableItem.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.list_item_deletable,
                        parent,
                        false
                    )
                return DeletableItemHolder(view)
            }
            ViewType.AddButton.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.button_add,
                        parent,
                        false
                    )
                return AddButtonViewHolder(view)
            }
            else -> throw Exception("Unknown viewType in DeletableListAdapter")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder){
            is DeletableItemHolder -> {
                val word = deletableItemsList.toList()[position]
                holder.bind(word)
            }
            is AddButtonViewHolder -> {
                holder.bind()
            }
        }
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

class DeletableItemHolder(private val view: View) : ViewHolder(view) {
    fun bind(translation: String) {
        val translationTextView = view.findViewById(R.id.translation_text) as TextView
        translationTextView.text = translation

        val deleteButton = view.findViewById(R.id.delete_button) as ImageButton

        deleteButton.setOnClickListener {
            (bindingAdapter as DeletableItemAdapter).apply {
                deletableItemsList.remove(translation)
                notifyItemRemoved(position)
            }
        }

    }
}

class AddButtonViewHolder(private val view: View): ViewHolder(view) {
    fun bind(){
        //callback
    }
}