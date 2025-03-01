@file:OptIn(ExperimentalFoundationApi::class)

package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shaker.TabItem
import com.example.shaker.data.recipes
import com.example.shaker.ui.GameplayViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun CenterSidebarPager(viewModel: MainViewModel, gameplayState: GameplayViewModel) {

    val listTabItem = listOf(
        TabItem("home", "screen_0"),
        TabItem("upgrade", "screen_1")
    )
    val pagerState_H: PagerState = rememberPagerState(
        initialPage = 0
    ) { listTabItem.size } // Horizontal pages
    val pagerState_V: PagerState = rememberPagerState(
        initialPage = viewModel.selectedUpgradeId.value
    ) { recipes.size } // Vertical pages

    Box(modifier = Modifier.fillMaxSize()) {
        CurrentPage(viewModel, gameplayState, pagerState_H, pagerState_V)
        MovingSideBar(
            viewModel,
            gameplayState,
            pagerState_H,
            pagerState_V,
            Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun CurrentPage(
    viewModel: MainViewModel,
    gameplayState: GameplayViewModel,
    pagerState_H: PagerState,
    pagerState_V: PagerState
) {

    var selectedTabItem: Int by remember { mutableStateOf(0) }

    HorizontalPager(
        state = pagerState_H,
        beyondBoundsPageCount = 1,
        key = { page -> page },
        modifier = Modifier.fillMaxSize()
    ) { page ->
        selectedTabItem = page
        when (page) {
            0 -> HomePage(Modifier.fillMaxWidth(0.8f), gameplayState)
            1 -> UpgradePage(
                pagerState_V,
                { upgradeId ->
                    viewModel.selectUpgrade(upgradeId)
                },
                gameplayState,
                Modifier
            )

            else -> Text("Unknown Screen")
        }
    }

}
