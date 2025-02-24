package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import com.example.shaker.TabItem
import com.example.shaker.data.upgrades


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpgradePage(pagerState: PagerState, onUpgradeChange: (Int) -> Unit, modifier: Modifier = Modifier) {
	LaunchedEffect(pagerState.currentPage){onUpgradeChange(pagerState.currentPage)}
	val fling = PagerDefaults.flingBehavior(
		state = pagerState,
		pagerSnapDistance = PagerSnapDistance.atMost(upgrades.size/4)
	)
	VerticalPager(
		state = pagerState,
		flingBehavior = fling,
		modifier = modifier
	){ id ->
		CurrentUpgrade(upgradeId = id)
	}
}

@Composable
fun CurrentUpgrade(upgradeId: Int, modifier: Modifier = Modifier){
	val upgrade = upgrades[upgradeId]
	Box(modifier.fillMaxSize()){
		Column(
			modifier = modifier.fillMaxWidth(0.8f).fillMaxHeight().align(Alignment.CenterEnd),
			verticalArrangement = Arrangement.Center,
		) {
			Text(
				stringResource(upgrade.name)
			)
			Image(
				painter = painterResource(upgrade.imageResourceId),
				alignment = Alignment.Center,
				contentScale = ContentScale.FillWidth,
				modifier = Modifier.fillMaxWidth()
					.padding(all = 5.dp)
					.graphicsLayer(transformOrigin = TransformOrigin.Center),
				contentDescription = null
			)
		}
	}
}

/*@Preview
@Composable
fun Upgrade_P(){
	UpgradePage(0)
}*/