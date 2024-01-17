package com.lithium.kotlin.dictionary.presentation.word

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.appComponent
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.presentation.di.AddWordFragmentScope
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*
import com.lithium.kotlin.dictionary.presentation.word.DeletableItemsListAdapter.DeletableItemAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PICK_IMAGE_AVATAR = 0

@AddWordFragmentScope
class AddWordFragment: Fragment() {

    interface CallBacks{
        fun onEditWordButtonClicked()
        fun onAddWordButtonClicked()
    }


    private lateinit var binding : FragmentWordBinding
    @Inject
    lateinit var  viewModel: AddWordViewModel
    private lateinit var deletableItemListAdapter: DeletableItemsListAdapter
    private var callBacks: CallBacks? = null

    private var iconPath: String = ""

    override fun onAttach(context: Context) {
        context.appComponent.addWordComponent().inject(this)

        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deletableItemListAdapter = DeletableItemsListAdapter(layoutInflater)
        viewModel.loadCategories()
        setupObservers()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_word,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTranslationsRv()
        setupCategoriesRv()

        setupAddButton()

        setupWordEditTextListener()
        setupTranslationsEditTextListener()
        setupCategoriesEditTextListener()

        setupWordIconListener()
    }

    override fun onDetach() {
        super.onDetach()

        callBacks = null
    }



    private fun setupObservers(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.translate.collect{
                    (binding.translationRecyclerView.adapter as DeletableItemAdapter).apply {

                        if(!data.contains(it) && it != "") {
                            data.add(it)
                            notifyItemInserted(data.size - 1)
                        }

                    }
                }
            }
        }
    }

    private fun setupTranslationsRv(){
        binding.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = deletableItemListAdapter.DeletableItemAdapter(mutableSetOf())
        }
    }

    private fun setupCategoriesRv(){
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = deletableItemListAdapter.DeletableItemAdapter(mutableSetOf())
        }
    }

    private fun setupAddButton(){
        binding.addButton.setOnClickListener{
            val _sequence = binding.wordEditText.text.toString()
            val _translation = (binding.translationRecyclerView.adapter as DeletableItemAdapter).data
            val _categories = (binding.categoriesRecyclerView.adapter as DeletableItemAdapter).data

            viewModel.addWord(
                Word(
                    sequence = _sequence,
                    translation = _translation,
                    categories = _categories,
                    photoFilePath = iconPath
                )
            )

            callBacks?.onAddWordButtonClicked()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupWordEditTextListener(){
        binding.wordEditText.apply{
            doAfterTextChanged {
                if (this.text.toString() == ""){
                    (binding.translationRecyclerView.adapter as DeletableItemAdapter).apply {
                        data.clear()
                        notifyDataSetChanged()
                    }
                }
                viewModel.translateRequest(this.text.toString())
            }
        }
    }

    private fun setupTranslationsEditTextListener(){
        binding.translationEditText.setOnEditorActionListener{
                editTextView, actionId, event ->
            deletableItemListAdapter.addCellToList(binding.translationRecyclerView.adapter as DeletableItemAdapter)(editTextView, actionId, event)
        }
    }

    private fun setupCategoriesEditTextListener(){
        binding.categoriesEditText.setOnEditorActionListener{
                editTextView, actionId, event ->
            deletableItemListAdapter.addCellToList(binding.categoriesRecyclerView.adapter as DeletableItemAdapter)(editTextView, actionId, event)
        }
    }

    private fun setupWordIconListener() {
        binding.newWordIcon.setOnClickListener {
            startSelectWordIconIntent()
        }
    }

    private fun startSelectWordIconIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_AVATAR
        )
    }

    @SuppressLint("Recycle")
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