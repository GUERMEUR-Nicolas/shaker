package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.shaker.data.upgrades
import kotlin.math.pow
import kotlin.math.roundToInt


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
			verticalArrangement = Arrangement.Center,
			modifier = modifier
				.fillMaxWidth(0.8f)
				.fillMaxHeight()
				.align(Alignment.CenterEnd),
		) {
			Text(
				fontSize = 12.sp,
				text = stringResource(upgrade.name)+" ("+upgrade.amount+")",
				textAlign = TextAlign.Center
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
			Row(
				horizontalArrangement = Arrangement.SpaceAround,
				modifier = modifier.fillMaxWidth()
			) {
				Button(
					onClick = {}
				) {
					Text("Buy 1 ("+upgrade.price+")")
				}
				Button(
					onClick = {}
				) {
					Text("Buy 10 ("+upgrade.price*1.15.pow(10).roundToInt()+")")
				}
			}
		}
	}
}

/*
@Preview
@Composable
fun Upgrade_P(){
	UpgradePage(0, {})
}*/

@Preview
@Composable
fun CurrentUpgrade_P(){
	CurrentUpgrade(upgradeId = 0)
}