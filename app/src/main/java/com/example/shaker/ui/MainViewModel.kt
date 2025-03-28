package com.example.shaker.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
	private val _selectedRecipeId = MutableStateFlow(0) // Default to the first item
	val selectedRecipeId: StateFlow<Int> = _selectedRecipeId.asStateFlow()

	fun selectRecipe(recipeId: Int) {
		viewModelScope.launch {
			_selectedRecipeId.emit(recipeId)
		}
	}
}