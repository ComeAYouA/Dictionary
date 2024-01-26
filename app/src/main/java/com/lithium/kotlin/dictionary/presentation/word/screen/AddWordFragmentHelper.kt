package com.lithium.kotlin.dictionary.presentation.word.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.presentation.word.AddWordFragmentScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@AddWordFragmentScope
class AddWordFragmentHelper @Inject constructor(
    private val viewModel: AddWordViewModel,
    private val deletableItemsListAdapter: DeletableItemsListAdapter,
    private val callBacks: AddWordFragment.CallBacks?
) {


    private var _binding : FragmentWordBinding? = null
    private val binding : FragmentWordBinding
        get() = _binding!!

    fun bindingInit(binding: FragmentWordBinding){
        this._binding = binding
    }

    fun Fragment.setupObservers(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.translate.collect{
                    (binding.translationRecyclerView.adapter as DeletableItemsListAdapter.DeletableItemAdapter).apply {

                        if(!data.contains(it) && it != "") {
                            data.add(it)
                            notifyItemInserted(data.size - 1)
                        }

                    }
                }
            }
        }
    }

    fun setupTranslationsRv(){
        binding.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = deletableItemsListAdapter.DeletableItemAdapter(mutableSetOf())
        }
    }

    fun setupCategoriesRv(){
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = deletableItemsListAdapter.DeletableItemAdapter(mutableSetOf())
        }
    }

    fun setupAddButton(){
        binding.addButton.setOnClickListener{
            val _sequence = binding.wordEditText.text.toString()
            val _translation = (binding.translationRecyclerView.adapter as DeletableItemsListAdapter.DeletableItemAdapter).data
            val _categories = (binding.categoriesRecyclerView.adapter as DeletableItemsListAdapter.DeletableItemAdapter).data

            viewModel.addWord(
                Word(
                    sequence = _sequence,
                    translation = _translation,
                    categories = _categories,
                    photoFilePath = viewModel.iconPath
                )
            )

            callBacks?.onAddWordButtonClicked()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setupWordEditTextListener(){
        binding.wordEditText.apply{
            doAfterTextChanged {
                if (this.text.toString() == ""){
                    (binding.translationRecyclerView.adapter as DeletableItemsListAdapter.DeletableItemAdapter).apply {
                        data.clear()
                        notifyDataSetChanged()
                    }
                }
                viewModel.translateRequest(this.text.toString())
            }
        }
    }

    fun setupTranslationsEditTextListener(){
        binding.translationEditText.setOnEditorActionListener{
                editTextView, actionId, event ->
            deletableItemsListAdapter.addCellToList(binding.translationRecyclerView.adapter as DeletableItemsListAdapter.DeletableItemAdapter)(editTextView, actionId, event)
        }
    }

    fun setupCategoriesEditTextListener() {
        binding.categoriesEditText.setOnEditorActionListener { editTextView, actionId, event ->
            deletableItemsListAdapter.addCellToList(binding.categoriesRecyclerView.adapter as DeletableItemsListAdapter.DeletableItemAdapter)(
                editTextView,
                actionId,
                event
            )
        }
    }
}