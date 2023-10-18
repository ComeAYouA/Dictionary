package com.lithium.kotlin.dictionary

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.view_models.EditDictionaryViewModel
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

private const val PICK_IMAGE_AVATAR = 0
private const val Arg_Word_Id = "word_id"

open class EditWordFragment: Fragment() {

    interface CallBacks{
        fun onEditWordButtonClicked()
    }

    private val editViewModel = EditDictionaryViewModel()
    private lateinit var binding : FragmentWordBinding

    private lateinit var iconPath: String
    private var callBacks: CallBacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callBacks = context as CallBacks?
        editViewModel.loadWord(arguments?.getSerializable(Arg_Word_Id) as UUID)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = DataBindingUtil.inflate<FragmentWordBinding>(
            inflater,
            R.layout.fragment_word,
            container,
            false
        )
        binding = view

        view.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        view.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

        view.newWordIcon.setOnClickListener{
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViewModel.wordLiveData.observe(
            viewLifecycleOwner
        ) { word ->
            word?.let { word: Word ->

                binding.apply {

                    wordEditText.setText(word.sequence)

                    translationRecyclerView.adapter = DeletableItemAdapter(word.translation)

                    categoriesRecyclerView.adapter = DeletableItemAdapter(word.categories)

                    iconPath = word.photoFilePath

                    if(iconPath != ""){
                        Picasso.with(context).load(File(iconPath)).into(newWordIcon)
                    }else{
                        Picasso.with(context).load(R.drawable.ic_empty_picture).into(newWordIcon)
                    }
                    addButton.setOnClickListener {
                        editViewModel.saveWord(word.copy(
                            sequence = wordEditText.text.toString(),
                            translation = (translationRecyclerView.adapter as DeletableItemAdapter).data,
                            categories = (categoriesRecyclerView.adapter as DeletableItemAdapter).data,
                            photoFilePath = iconPath
                        ))
                        callBacks?.onEditWordButtonClicked()
                    }
                }
            }
        }

    }

    override fun onDetach() {
        super.onDetach()

        callBacks = null
    }

    companion object {
        fun newInstance(wordId: UUID): EditWordFragment {
            val args = Bundle().apply {
                putSerializable(Arg_Word_Id, wordId)
            }
            return EditWordFragment().apply {
                arguments = args
            }
        }
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

    @Deprecated("Deprecated in Java")
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
                Picasso.with(requireContext()).load(File(iconPath)).into(binding.newWordIcon)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}