package com.example.shaker.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.shaker.R
import com.example.shaker.TabItem
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel
import com.example.shaker.ui.MainViewModel

@Composable
fun CenterSidebarPager(
	viewModel: MainViewModel,
	gameplayState: GameplayViewModel,
	startPage: Int = 0
) {

	val listTabItem = listOf(
		TabItem("home", "screen_0"),
		TabItem("recipe", "screen_1")
	)
	val pagerState_H: PagerState = rememberPagerState(
		initialPage = startPage
	) { listTabItem.size } // Horizontal pages
	val pagerState_V: PagerState = rememberPagerState(
		initialPage = viewModel.selectedRecipeId.value
	) { allRecipes.size } // Vertical pages

	Box(modifier = with(Modifier) {
		fillMaxSize()
			.paint(
				painter = painterResource(R.drawable.wood),
				contentScale = ContentScale.Crop
			)
	}
	) {
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
	pagerState_V: PagerState,
) {
	var selectedTabItem: Int by remember { mutableStateOf(0) }
	val advancement = gameplayState.advancementState.collectAsState()
	val bgColor = MaterialTheme.colorScheme.surfaceContainerLowest
	HorizontalPager(
		state = pagerState_H,
		beyondViewportPageCount = 1,
		key = { page -> page },
		userScrollEnabled =
		advancement.value.getAdvancement("showSideBar")
				&& !advancement.value.getAdvancement("shaking"),
		modifier = Modifier
			.fillMaxSize()
	) { page ->
		selectedTabItem = page
		when (page) {
			0 -> HomePage(
				Modifier.fillMaxWidth(if (advancement.value.getAdvancement("showSideBar")) 0.8f else 1f),
				gameplayState
			)

			1 -> RecipesPage(
				pagerState_V,
				{ recipeId ->
					viewModel.selectRecipe(recipeId)
					viewModel.selectUpgrade(null)
				},
				gameplayState,
				Modifier,
				viewModel
			)

			else -> Text("\uD83E\uDD86\uFE0F")
		}
	}

}
