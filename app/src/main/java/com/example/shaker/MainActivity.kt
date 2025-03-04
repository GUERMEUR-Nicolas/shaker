package com.example.shaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shaker.home.MainViewModel
import com.example.shaker.ui.theme.ShakerTheme
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.example.shaker.home.CenterSidebarPager
import com.example.shaker.ui.GameplayViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()
	private val gameplayState: GameplayViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//enableEdgeToEdge()
		setContent {
			ShakerTheme(darkTheme = false) {
				CenterSidebarPager(viewModel,gameplayState)
				LaunchedEffect(Unit) {
					while (true) {
						val cycleDuration = 500L
						delay(cycleDuration)
						gameplayState.Increment(1f)
					}
				}
			}
		}
	}

}
@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreview(){
	val viewModel = MainViewModel()
	val gameplayState = GameplayViewModel()
	ShakerTheme(darkTheme = false) {
		CenterSidebarPager(viewModel,gameplayState)
		LaunchedEffect(Unit) {
			while (true) {
				delay(160L) // Delay for 1 second
				gameplayState.Increment(160L / 1000f)
			}
		}
	}
}
data class TabItem(val name:String, val screen: String)