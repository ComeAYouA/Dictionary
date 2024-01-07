package com.lithium.kotlin.dictionary.presentation.word

import android.content.Intent
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R

private const val PICK_IMAGE_AVATAR = 0

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
    fun addCellToList(listAdapter: EditWordAdapter.DeletableItemAdapter): (TextView, Int, KeyEvent?) -> Boolean = {
            view, actionId, _ ->
        if(actionId == EditorInfo.IME_ACTION_NEXT){
            listAdapter.apply {
                val input = view.text.toString()
                if(!data.contains(input)) {
                    data.add(input)
                }
                notifyItemInserted(data.size - 1)
            }
            view.setText(R.string.empty)
            true
        }else{
            true
        }
    }
}

fun RecyclerView.prepareDeletableItemsRecyclerView(editWordAdapter: EditWordAdapter){
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = editWordAdapter.DeletableItemAdapter(mutableSetOf())
}

fun Fragment.startSelectWordIconIntent(){
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_PICK
    startActivityForResult(
        Intent.createChooser(intent, "Select Picture"),
        PICK_IMAGE_AVATAR
    )
}