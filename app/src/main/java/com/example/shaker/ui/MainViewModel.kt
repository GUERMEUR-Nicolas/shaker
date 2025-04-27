package com.example.shaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaker.data.Upgrade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _selectedRecipeId = MutableStateFlow(0) // Default to the first item
    val selectedRecipeId: StateFlow<Int> = _selectedRecipeId.asStateFlow()
    private val _selectedUpgrade: MutableStateFlow<Upgrade?> =
        MutableStateFlow(null) // Default to the first item
    val selectedUpgrade: StateFlow<Upgrade?> = _selectedUpgrade.asStateFlow()
    fun selectRecipe(recipeId: Int) {
        viewModelScope.launch {
            _selectedRecipeId.emit(recipeId)
        }
    }

    fun selectUpgrade(upgrade: Upgrade?) {
        viewModelScope.launch {
            _selectedUpgrade.emit(upgrade)
        }
    }
}