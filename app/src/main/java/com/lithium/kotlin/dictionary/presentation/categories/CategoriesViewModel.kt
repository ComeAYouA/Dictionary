package com.lithium.kotlin.dictionary.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.data.repository.WordsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: WordsRepositoryImpl
): ViewModel() {
    private val _categories: MutableStateFlow<List<Category>> = MutableStateFlow(listOf())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().flowOn(Dispatchers.IO).collect { categories ->
                _categories.value = categories
            }
        }
    }
}