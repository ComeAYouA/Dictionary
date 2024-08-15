package com.lithium.kotlin.dictionary.features.add_word.screen

import android.annotation.SuppressLint
import android.util.Log
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

fun AddWordFragment.setupObservers(
){
    lifecycleScope.launch{
        repeatOnLifecycle(Lifecycle.State.STARTED) {

            launch {
                viewModel.translation.collect{ translation ->
                    Log.d("myTag", translation.toString())
                    translationRVAdapter.apply {
                        setDeletableItemsList(translation)
                    }
                }
            }

            launch {
                viewModel.categories.collect{ categories ->
                    categoriesRVAdapter.apply {
                        Log.d("myTag", categories.toString())

                        setDeletableItemsList(categories)
                    }
                }
            }
        }
    }
}

fun AddWordFragment.setupTranslationsRv(){
    binding.translationRecyclerView.apply {
        layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        adapter = translationRVAdapter.apply {
            this.setDeletableItemsList(viewModel.translation.value)
            onDeleteListener = object: DeletableItemAdapter.OnDeleteListener {
                override fun onItemDeleted(item: String) {
                    viewModel.removeTranslation(item)
                }
            }
        }
    }
}

fun AddWordFragment.setupCategoriesRv(){
    binding.categoriesRecyclerView.apply {
        layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        adapter = categoriesRVAdapter.apply {
            this.setDeletableItemsList(viewModel.categories.value)

            onDeleteListener = object: DeletableItemAdapter.OnDeleteListener {
                override fun onItemDeleted(item: String) {
                    viewModel.removeCategory(item)
                }
            }
        }
    }
}

fun AddWordFragment.setupAddButton(){
    binding.addButton.setOnClickListener{
        viewModel.addWord()
        callBacks?.onAddWordButtonClicked()
    }
}

fun AddWordFragment.setupWordEditTextListener(){
    binding.wordEditText.apply{
        doAfterTextChanged { text ->
            val word = text.toString()
            viewModel.updateWord(word)
        }
    }
}

fun AddWordFragment.setupTranslationsEditTextListener(){
    binding.translationEditText.setOnEditorActionListener { view, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            viewModel.addTranslation(view.text.toString())
            view.setText(R.string.empty)
        }
        true
    }
}

fun AddWordFragment.setupCategoriesEditTextListener() {
    binding.categoriesEditText.setOnEditorActionListener { view, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            viewModel.addCategory(view.text.toString())
            view.setText(R.string.empty)
        }
        true
    }
}