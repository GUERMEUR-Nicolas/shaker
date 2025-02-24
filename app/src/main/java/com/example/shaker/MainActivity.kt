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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.shaker.home.HomePage
import com.example.shaker.home.MainViewModel
import com.example.shaker.home.Sidebar
import com.example.shaker.home.UpgradePage
import com.example.shaker.ui.theme.ShakerTheme
import androidx.activity.viewModels

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			ShakerTheme(darkTheme = false) {
				val listTabItem = listOf(
					TabItem("home", "screen_0"),
					TabItem("upgrade", "screen_1")
				)
				val align = listOf(Alignment.CenterEnd, Alignment.CenterStart)
				var selectedTabItem: Int by remember { mutableStateOf(0) }
				var selectedUpgradeItem: Int by remember { mutableStateOf(0) }
				val pagerState = rememberPagerState(initialPage=0) { listTabItem.size }

				LaunchedEffect(pagerState.currentPage){
					selectedTabItem = pagerState.currentPage
				}

				Box(){
					HorizontalPager(
						state = pagerState,
						Modifier.fillMaxSize()
					){ _ ->
						when (selectedTabItem) {
							0 -> HomePage(Modifier.fillMaxWidth(0.8f))
							1 -> UpgradePage(
								viewModel.selectedUpgradeId.value,
								Modifier
							)
							else -> Text("Unknown Screen")
						}
					}
					Sidebar(
						onUpgradeClick = { upgradeId ->
							viewModel.selectUpgrade(upgradeId)
						},
						Modifier
							.zIndex(1f)
							.fillMaxWidth(.2f)
							.align(align[selectedTabItem])
					)
				}
			}
		}
	}
}

data class TabItem(val name:String, val screen: String)