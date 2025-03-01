package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.shaker.data.Recipe
import com.example.shaker.data.recipes
import com.example.shaker.ui.GameplayViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpgradePage(
    pagerState: PagerState,
    onUpgradeChange: (Int) -> Unit,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier
) {
    val recipesState = gameState.recipes.collectAsState()//To ensure recomposition on the recipes changes
    LaunchedEffect(pagerState.currentPage) { onUpgradeChange(pagerState.currentPage) }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(recipes.size / 4)
    )
    VerticalPager(
        state = pagerState,
        flingBehavior = fling,
        modifier = modifier
    ) { id ->
        CurrentUpgrade(upgradeId = id, gameState = gameState)
    }
}

@Composable
fun CurrentUpgrade(
    upgradeId: Int,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier
) {
    val recipe = recipes[upgradeId]
    Box(modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
        ) {
            RecipeInfo(recipe, gameState = gameState, 40.sp,true, modifier)
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier.fillMaxWidth()
            ) {
                BuyButton(recipe, 1, gameState)
                BuyButton(recipe, 10, gameState)
            }
        }
    }
}

@Composable
fun BuyButton(recipe: Recipe, amount: Long, gameplayViewModel: GameplayViewModel) {
    Button(
        onClick = { gameplayViewModel.ForceBuy(recipe, amount) },
        Modifier.clickable { gameplayViewModel.canBuy(recipe, amount) }
    ) {
        Text(
            "Buy $amount (" + (gameplayViewModel.GetNextCost(
                recipe,
                amount
            )).ValueAsString() + ")"
        )
    }
}

@Composable
fun RecipeInfo(recipe: Recipe, gameState: GameplayViewModel, spBase : TextUnit, showName: Boolean, modifier: Modifier) {
    if (showName) {
        Text(
            fontSize = spBase,
            text = stringResource(recipe.name) + " (" + gameState.GetRecipeAmount(recipe) + ")",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        PerSecondText(recipe.amount, spBase*.8f, modifier.fillMaxWidth())
    }
    Image(
        painter = painterResource(recipe.imageResourceId),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 5.dp)
            .graphicsLayer(transformOrigin = TransformOrigin.Center),
        contentDescription = null
    )
    /*if (showName) {
        Text(
            text = stringResource(upgrade.name),
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            modifier = modifier.padding(horizontal = 5.dp)
        )
    }
    Image(
        painter = painterResource(upgrade.imageResourceId),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .padding(all = 5.dp)
            .graphicsLayer(transformOrigin = TransformOrigin.Center),
        contentDescription = null
    )*/
}
/*
@Preview
@Composable
fun Upgrade_P(){
	UpgradePage(0, {})
}*/

@Preview
@Composable
fun CurrentUpgrade_P() {
    val gameplayViewModel = GameplayViewModel()
    CurrentUpgrade(upgradeId = 0, gameState = gameplayViewModel)
}