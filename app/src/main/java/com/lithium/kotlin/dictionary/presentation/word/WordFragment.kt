package com.lithium.kotlin.dictionary.presentation.word

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Word
import com.lithium.kotlin.dictionary.presentation.categories.CategoriesFragment
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*
import com.lithium.kotlin.dictionary.presentation.word.EditWordAdapter.DeletableItemAdapter
import kotlin.collections.ArrayDeque

private const val PICK_IMAGE_AVATAR = 0
private const val Arg_Word_Id = "word_id"

class EditWordFragment: Fragment() {

    interface CallBacks{
        fun onEditWordButtonClicked()
        fun onAddWordButtonClicked()
    }


    private val editViewModel: EditDictionaryViewModel by lazy{
        ViewModelProvider(this)[EditDictionaryViewModel::class.java]
    }
    private lateinit var binding : FragmentWordBinding
    private lateinit var editWordAdapter: EditWordAdapter

    private var iconPath: String = ""
    private var callBacks: CallBacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editWordAdapter = EditWordAdapter(layoutInflater)

        editViewModel.loadCategories()

        arguments?.getSerializable(Arg_Word_Id)?.let {
            editViewModel.loadWord(it as UUID)
        }

        lifecycleScope.launchWhenStarted {
            editViewModel.translate.collect{
                (binding.translationRecyclerView.adapter as DeletableItemAdapter).apply {
                    if(!data.contains(it)) {
                        data.add(it)
                        notifyItemInserted(data.size - 1)
                    }

                }
            }
        }

        lifecycleScope.launchWhenStarted {

            editViewModel.word.collect{ word: Word ->

                binding.apply {

                    wordEditText.setText(word.sequence)

                    translationRecyclerView.adapter = editWordAdapter.DeletableItemAdapter(word.translation)

                    categoriesRecyclerView.adapter = editWordAdapter.DeletableItemAdapter(word.categories)

                    iconPath = word.photoFilePath

                    if(iconPath != ""){
                        Picasso.with(context).load(File(iconPath)).fit().into(newWordIcon)
                    }else{
                        Picasso.with(context).load(R.drawable.ic_empty_picture).into(newWordIcon)
                    }

                    addButton.setOnClickListener {
                        editViewModel.saveWord(word.copy(
                            sequence = wordEditText.text.toString(),
                            translation = (translationRecyclerView.adapter as DeletableItemAdapter).data,
                            categories = (categoriesRecyclerView.adapter as DeletableItemAdapter).data,
                            photoFilePath = iconPath))
                        callBacks?.onEditWordButtonClicked()
                    }
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate<FragmentWordBinding>(
            inflater,
            R.layout.fragment_word,
            container,
            false
        )

        binding.wordEditText.apply{
            doAfterTextChanged {
                editViewModel.translateRequest(this.text.toString())
            }
        }

        binding.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = editWordAdapter.DeletableItemAdapter(mutableSetOf())
        }
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = editWordAdapter.DeletableItemAdapter(mutableSetOf())
        }

        binding.translationEditText.setOnEditorActionListener{
                view, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_NEXT){
                (binding.translationRecyclerView.adapter as DeletableItemAdapter).apply {
                    val input = view.text.toString()
                    if(!data.contains(input)) {
                        this.data.add(input)
                    }
                    notifyItemInserted(data.size - 1)
                }
                view.setText(R.string.empty)
                true
            }else{
                true
            }
        }

        binding.categoriesEditText.setOnEditorActionListener{
                view, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_NEXT){
                (binding.categoriesRecyclerView.adapter as DeletableItemAdapter).apply {
                    val input = view.text.toString()
                    if(!data.contains(input)) {
                        this.data.add(input)
                    }
                    notifyItemInserted(data.size - 1)
                }
                view.setText(R.string.empty)
                true
            }else{
                true
            }
        }

        binding.newWordIcon.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_AVATAR
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addButton.setOnClickListener{
            editViewModel.addWord(
                Word(
                    sequence = binding.wordEditText.text.toString(),
                    translation = (binding.translationRecyclerView.adapter as DeletableItemAdapter).data,
                    categories = (binding.categoriesRecyclerView.adapter as DeletableItemAdapter).data,
                    photoFilePath = iconPath
                )
            )
            callBacks?.onAddWordButtonClicked()
        }

    }

    override fun onDetach() {
        super.onDetach()

        callBacks = null
    }

    companion object {
        fun newInstance(wordId: UUID? = null): EditWordFragment {
            val args = Bundle().apply {
                putSerializable(Arg_Word_Id, wordId)
            }
            return EditWordFragment().apply {
                arguments = args
            }
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