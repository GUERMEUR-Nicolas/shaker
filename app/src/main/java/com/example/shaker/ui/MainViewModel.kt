package com.example.shaker.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
	private val _selectedUpgradeId = MutableStateFlow(0) // Default to the first item
	val selectedUpgradeId: StateFlow<Int> = _selectedUpgradeId.asStateFlow()

	fun selectUpgrade(upgradeId: Int) {
		viewModelScope.launch {
			_selectedUpgradeId.emit(upgradeId)
		}
	}
}