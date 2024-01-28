package com.lithium.kotlin.dictionary.features.categories.sreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.features.categories.di.CategoriesFragmentScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@CategoriesFragmentScope
class CategoriesViewModel @Inject constructor(
    private val repository: WordDao
): ViewModel() {
    private val _categories: MutableStateFlow<List<Category>> = MutableStateFlow(listOf())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()


    init {
        Log.d("myTag", "CategoriesViewModel init")
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().flowOn(Dispatchers.IO).collect { categories ->
                _categories.value = categories
            }
        }
    }
}