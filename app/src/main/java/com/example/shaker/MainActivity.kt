package com.example.shaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shaker.home.MainViewModel
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
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
			AppTheme(darkTheme = false) {
				CenterSidebarPager(viewModel,gameplayState)
				LaunchedEffect(Unit) {
					while (true) {
						val cycleDuration = 2000L
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
fun AppPreviewLight(){
	val viewModel = MainViewModel()
	val gameplayState = GameplayViewModel()
	AppTheme(darkTheme = false) {
		CenterSidebarPager(viewModel,gameplayState)
		LaunchedEffect(Unit) {
			while (true) {
				delay(160L) // Delay for 1 second
				gameplayState.Increment(160L / 1000f)
			}
		}
	}
}
/*@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewDark(){
	val viewModel = MainViewModel()
	val gameplayState = GameplayViewModel()
	AppTheme(darkTheme = true) {
		CenterSidebarPager(viewModel,gameplayState)
		LaunchedEffect(Unit) {
			while (true) {
				delay(160L) // Delay for 1 second
				gameplayState.Increment(160L / 1000f)
			}
		}
	}
}*/
data class TabItem(val name:String, val screen: String)