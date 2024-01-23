package com.lithium.kotlin.dictionary.presentation.word.screen

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.presentation.word.AddWordFragmentScope
import javax.inject.Inject


@AddWordFragmentScope
class DeletableItemsListAdapter @Inject constructor(
    private val layoutInflater: LayoutInflater
) {

    init {
        Log.d("myTag", "DeletableItemsListAdapter init")
    }

    inner class DeletableItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(translation: String) {
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

    inner class DeletableItemAdapter(val data: MutableSet<String>) :
        RecyclerView.Adapter<DeletableItemHolder>() {
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

    fun addCellToList(listAdapter: DeletableItemAdapter): (TextView, Int, KeyEvent?) -> Boolean =
        { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                listAdapter.apply {
                    val input = view.text.toString()

                    if (!data.contains(input)) {
                        data.add(input)
                    }

                    notifyItemInserted(data.size - 1)
                }

                view.setText(R.string.empty)
            }
            true
        }
}