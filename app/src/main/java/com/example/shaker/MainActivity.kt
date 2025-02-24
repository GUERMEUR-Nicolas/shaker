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
import com.example.shaker.home.UpgradeSidebar
import com.example.shaker.home.UpgradePage
import com.example.shaker.ui.theme.ShakerTheme
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.shaker.data.upgrades
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//enableEdgeToEdge()
		setContent {
			ShakerTheme(darkTheme = false) {
				val listTabItem = listOf(
					TabItem("home", "screen_0"),
					TabItem("upgrade", "screen_1")
				)
				//val align = listOf(Alignment.CenterEnd, Alignment.CenterStart)
				//var selectedTabItem: Int by remember { mutableStateOf(0) }
				val pagerState_H = rememberPagerState(
					initialPage=0
				) { listTabItem.size } // Horizontal pages
				val pagerState_V = rememberPagerState(
					initialPage=viewModel.selectedUpgradeId.value
				) { upgrades.size } // Vertical pages
				val coroutineScope = rememberCoroutineScope() // scroll to page
				val selectedUpgradeId by viewModel.selectedUpgradeId.collectAsState()
				var selectedTabItem: Int by remember { mutableStateOf(0) }

				val density = LocalDensity.current
				val configuration = LocalConfiguration.current
				var sidebarWidthPx by remember { mutableStateOf(0) }
				val sidebarWidthDp = with(density) { sidebarWidthPx.toDp() }

				val offset = -(configuration.screenWidthDp.dp - sidebarWidthDp) * (pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction)

				Box(modifier = Modifier.fillMaxSize()){
					HorizontalPager(
						state = pagerState_H,
						beyondBoundsPageCount = 1,
						key = { page -> page },
						modifier = Modifier.fillMaxSize()
					) { page ->
						selectedTabItem = page
						when (page) {
							0 -> HomePage(Modifier.fillMaxWidth(0.8f))
							1 -> UpgradePage(
								pagerState_V,
								{ upgradeId ->
									viewModel.selectUpgrade(upgradeId)
								},
								Modifier
							)

							else -> Text("Unknown Screen")
						}
					}
					UpgradeSidebar(
						selectedUpgradeId = selectedUpgradeId,
						onUpgradeClick = { upgradeId ->
							viewModel.selectUpgrade(upgradeId)
							coroutineScope.launch {
								pagerState_V.animateScrollToPage(upgradeId)
							}
						},
						modifier = Modifier
							//.zIndex(1f)
							.fillMaxWidth(.2f)
							.onGloballyPositioned { coordinates ->
								sidebarWidthPx = coordinates.size.width
							}
							.offset { IntOffset(offset.roundToPx(), 0) }
							.align(Alignment.CenterEnd)
					)
				}
			}
		}
	}
}

data class TabItem(val name:String, val screen: String)