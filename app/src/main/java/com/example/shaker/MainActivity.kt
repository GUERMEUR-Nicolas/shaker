package com.example.shaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.shaker.home.Home
import com.example.shaker.home.Sidebar
import com.example.shaker.home.Upgrade
import com.example.shaker.ui.theme.ShakerTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			ShakerTheme {
				Main(
					modifier = Modifier
				)
			}
		}
	}
}

data class TabItem(val name:String, val screen: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Main(modifier: Modifier = Modifier){
	val listTabItem = listOf(
		TabItem("home", "screen_0"),
		TabItem("upgrade", "screen_1")
	)
	val align = listOf(Alignment.CenterEnd, Alignment.CenterStart)
	var selectedTabItem: Int by remember { mutableStateOf(0) }
	val pagerState = rememberPagerState(initialPage=0) { listTabItem.size }

	LaunchedEffect(pagerState.currentPage){
		selectedTabItem = pagerState.currentPage
	}

	Box(modifier = modifier){
		HorizontalPager(
			state = pagerState,
			modifier.fillMaxSize()
		){ page ->
			when (selectedTabItem) {
				0 -> Home(Modifier.fillMaxWidth(0.8f))
				1 -> Upgrade(Modifier.fillMaxWidth(0.8f))
				else -> Text("Unknown Screen")
			}
		}
		Sidebar(Modifier
			.zIndex(1f)
			.fillMaxWidth(.2f)
			.align(align[selectedTabItem])
		)
	}
}

@Preview
@Composable
fun Main_P(){
	ShakerTheme {
		Main()
	}
}