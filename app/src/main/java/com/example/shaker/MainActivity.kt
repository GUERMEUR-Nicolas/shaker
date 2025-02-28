package com.example.shaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shaker.home.MainViewModel
import com.example.shaker.ui.theme.ShakerTheme
import androidx.activity.viewModels
import com.example.shaker.home.CenterSidebarPager
import com.example.shaker.ui.GameplayViewModel

class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//enableEdgeToEdge()
		setContent {
			ShakerTheme(darkTheme = false) {
				CenterSidebarPager(viewModel)
			}
		}
	}
}

data class TabItem(val name:String, val screen: String)