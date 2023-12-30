package com.lithium.kotlin.dictionary.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import com.lithium.kotlin.dictionary.data.repositories.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CategoriesViewModel: ViewModel() {
    private val repository = WordsRepository.get()
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