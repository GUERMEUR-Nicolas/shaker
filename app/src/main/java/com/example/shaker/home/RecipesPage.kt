package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Recipe
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel
import com.example.ui.theme.bodyFontFamily


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpgradePage(
    pagerState: PagerState,
    onUpgradeChange: (Int) -> Unit,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) { onUpgradeChange(pagerState.currentPage) }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(allRecipes.size / 4)
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
    val recipe = allRecipes[upgradeId]
    Box(modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
        ) {
            RecipeInfo(recipe, gameState = gameState, 40.sp, true, false, modifier)
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
fun BuyButton(recipe: Recipe, amountToBuy: Long, gameplayViewModel: GameplayViewModel) {
    var recipes = gameplayViewModel.recipes.collectAsState()
    var money = gameplayViewModel.moneyState.collectAsState()
    Button(
        onClick = { gameplayViewModel.ForceBuy(recipe, amountToBuy) },
        enabled = recipes.value.canBuy(recipe, amountToBuy, money.value.current)
    ) {
        Text(
            "Buy $amountToBuy (" + (recipes.value.GetNextCost(
                recipe,
                amountToBuy
            )).ValueAsString() + ")"
        )
    }
}

@Composable
fun RecipeInfo(
    recipe: Recipe,
    gameState: GameplayViewModel,
    spBase: TextUnit,
    showName: Boolean,
    inSidebar: Boolean,
    modifier: Modifier
) {
    val recipes = gameState.recipes.collectAsState()
    val recipeAmount = recipes.value.GetRecipeAmount(recipe)
    if (showName) {
        Text(
            fontSize = spBase,
            text = stringResource(recipe.name) + " (" + recipeAmount + ")" + stringResource(R.string.money_per_cycle, recipe.generating*recipeAmount) ,
            color = if (inSidebar) Color.Black else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
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
    if (showName && !inSidebar) {
        PerSecondText(recipe.generating, spBase * .8f, modifier.fillMaxWidth())
    }
}
/*
@Preview
@Composable
fun Upgrade_P(){
	UpgradePage(0, {})
}*/

@Preview(widthDp = 600, heightDp = 600)
@Composable
fun CurrentUpgrade_P() {
    val gameplayViewModel = GameplayViewModel()
    CurrentUpgrade(upgradeId = 0, gameState = gameplayViewModel)
}