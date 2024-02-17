package com.lithium.kotlin.dictionary.features.add_word.screen

import android.annotation.SuppressLint
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.features.add_word.di.AddWordScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AddWordScope
class AddWordFragmentBindingAdapter @Inject constructor(
    private val callBacks: AddWordFragment.CallBacks?
) {


    private var _binding : FragmentWordBinding? = null
    private val binding : FragmentWordBinding
        get() = _binding!!

    private val translationsRVAdapter: DeletableItemAdapter by lazy { DeletableItemAdapter() }
    private val categoriesRVAdapter: DeletableItemAdapter by lazy { DeletableItemAdapter() }

    fun bindingInit(binding: FragmentWordBinding){
        this._binding = binding
    }

    fun Fragment.setupObservers(viewModel: AddWordViewModel){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.translation.collect{ translation ->
                    translationsRVAdapter.apply {
                        setDeletableItemsList(
                            mutableSetOf(translation)
                        )
                    }
                }
            }
        }
    }

    fun setupTranslationsRv(){
        binding.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = translationsRVAdapter
        }
    }

    fun setupCategoriesRv(){
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = categoriesRVAdapter
        }
    }

    fun setupAddButton(viewModel: AddWordViewModel){
        binding.addButton.setOnClickListener{
            val _sequence = binding.wordEditText.text.toString()
            val _translation = translationsRVAdapter.deletableItemsList
            val _categories = categoriesRVAdapter.deletableItemsList

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
    fun setupWordEditTextListener(viewModel: AddWordViewModel){
        binding.wordEditText.apply{
            doAfterTextChanged {
                if (this.text.toString() == ""){
                    translationsRVAdapter.setDeletableItemsList(mutableSetOf())
                }
                viewModel.translateRequest(this.text.toString())
            }
        }
    }

    fun setupTranslationsEditTextListener(){
        binding.translationEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                translationsRVAdapter.addCellToList(view.text.toString())
                view.setText(R.string.empty)
            }
            true
        }
    }

    fun setupCategoriesEditTextListener() {
        binding.categoriesEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                categoriesRVAdapter.addCellToList(view.text.toString())
                view.setText(R.string.empty)
            }
            true
        }
    }
}