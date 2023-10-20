package com.lithium.kotlin.dictionary.views

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.view_models.EditDictionaryViewModel
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.models.Word


private const val  TAG = "WordFragment"
private const val PICK_IMAGE_AVATAR = 0
class AddWordFragment: Fragment() {

    interface CallBacks{
        fun onAddWordButtonClicked()
    }

    private var callBacks: CallBacks? = null
    private lateinit var iconView: ImageButton
    private var iconPath = ""
    private val editViewModel = EditDictionaryViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = context as CallBacks?
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editViewModel.loadCategories()

        val view = DataBindingUtil.inflate<FragmentWordBinding>(
            inflater,
            R.layout.fragment_word,
            container,
            false
        )
        view.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = DeletableItemAdapter(mutableSetOf())
        }
        view.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = DeletableItemAdapter(mutableSetOf())
        }
        view.translationEditText.apply {
            setOnEditorActionListener{
                    _, actionId, _ ->

                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    (view.translationRecyclerView.adapter as DeletableItemAdapter).apply {
                        val input = text.toString()
                        if(!data.contains(input)) {
                            data.add(input)
                            notifyItemInserted(data.size - 1)
                        }
                    }
                    setText(R.string.empty)
                    true
                }else{
                    false
                }
            }
        }

        view.categoriesEditText.apply {
            setOnEditorActionListener{
                    _, actionId, _ ->

                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    (view.categoriesRecyclerView.adapter as DeletableItemAdapter).apply {
                        val input = text.toString()
                        if(!data.contains(input)) {
                            data.add(input)
                            notifyItemInserted(data.size - 1)
                        }
                    }
                    setText(R.string.empty)
                    true
                }else{
                    false
                }
            }
        }

        view.apply {
            addButton.setOnClickListener {
                editViewModel.addWord(
                    Word(
                    sequence = wordEditText.text.toString(),
                    translation = (translationRecyclerView.adapter as DeletableItemAdapter).data,
                    categories = (categoriesRecyclerView.adapter as DeletableItemAdapter).data,
                    photoFilePath = iconPath
                    ),
                    viewLifecycleOwner
                )
                callBacks?.onAddWordButtonClicked()
            }
        }
        iconView = view.newWordIcon
        iconView.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_AVATAR
            )
        }
        return view.root
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    private inner class DeletableItemHolder(private val view: View): RecyclerView.ViewHolder(view){
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
    private inner class DeletableItemAdapter(val data: MutableSet<String>): RecyclerView.Adapter<DeletableItemHolder>(){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            requestCode == PICK_IMAGE_AVATAR && data != null -> {
                val selectedImage: Uri = data.data!!
                val c: Cursor = requireActivity().contentResolver.query(selectedImage,null,null,null,null)!!
                c.moveToFirst()
                val path: String = c.getString(
                    if(c.getColumnIndex(MediaStore.MediaColumns.DATA) >= 0){
                        c.getColumnIndex(MediaStore.MediaColumns.DATA)
                    }else{
                        0
                    }
                )
                iconPath = path
                iconView.setImageDrawable(Drawable.createFromPath(iconPath))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}