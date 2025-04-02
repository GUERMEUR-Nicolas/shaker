package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Recipe
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipesPage(
    pagerState: PagerState,
    onRecipeChange: (Int) -> Unit,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) { onRecipeChange(pagerState.currentPage) }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(allRecipes.size / 4)
    )
    VerticalPager(
        state = pagerState,
        flingBehavior = fling,
        modifier = modifier
    ) { id ->
        CurrentRecipe(recipeID = id, gameState = gameState)
    }
}

@Composable
fun CurrentRecipe(
    recipeID: Int,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier
) {
    val recipe = allRecipes[recipeID]
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
                RecipeBuyButton(recipe, 1, gameState)
                RecipeBuyButton(recipe, 10, gameState)
            }
            Spacer(
                modifier = Modifier.height(15.dp)
            )
            UpgradeRow(recipe, modifier, 75.dp, gameState = gameState)
        }
    }
}

@Composable
fun UpgradeRow(recipe: Recipe, modifier: Modifier, dp: Dp, gameState: GameplayViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        //TODO fetch the actual upgrades of the recipe
		for(i in recipe.upgrades.indices){
			UpgradeWithButton(recipe, i, modifier, dp, gameState)
		}
        /*UpgradeWithButton(allUpgrades[0], modifier, dp)
        UpgradeWithButton(allUpgrades[1], modifier, dp)
        UpgradeWithButton(allUpgrades[2], modifier, dp)
        UpgradeWithButton(allUpgrades[3], modifier, dp)*/
    }
}

@Composable
fun UpgradeWithButton(recipe: Recipe, upgradeID: Int, modifier: Modifier, dp: Dp, gameState: GameplayViewModel) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        UpgradeIcon(recipe.upgrades[upgradeID].first, Modifier.size(dp))
        UpgradeBuyButton(recipe, upgradeID, modifier, gameState)
    }
}

/*@Composable
@Preview(widthDp = 600, heightDp = 100)
fun UpgradeRowPreview() {
    UpgradeRow(allRecipes[0], Modifier.size(100.dp), 75.dp, gameState)
}

@Composable
@Preview(widthDp = 100, heightDp = 100)
fun UpgradeWithButtonPreview() {
    UpgradeWithButton(allRecipes[0], 2, Modifier, 75.dp, gameState)
}*/

@Composable
fun GenericBuyButton(
    onClick: () -> Unit,
    enable: Boolean,
    text: String
) {
    Button(
        onClick = onClick,
        enabled = enable
    ) {
        Text(
            text
        )
    }
}

@Composable
fun UpgradeBuyButton(recipe: Recipe, upgradeID: Int, modifier: Modifier, gameplayViewModel: GameplayViewModel) {
    //TODO bind viewModel/money and enable and procesed the onClick with money dedeuciton and stuff like in the recipeBuyButton
    GenericBuyButton(
        onClick = {gameplayViewModel.ForceBuy(recipe, upgradeID)},
        enable = true,
        //text = upg.cost.GetCost((upg.level + 1).toLong()).ValueAsString()
		text = recipe.upgrades[upgradeID].first.cost.ValueAsString()
    )
}

@Composable
fun RecipeBuyButton(recipe: Recipe, amountToBuy: Long, gameplayViewModel: GameplayViewModel) {
    var recipes = gameplayViewModel.recipes.collectAsState()
    var money = gameplayViewModel.moneyState.collectAsState()
    GenericBuyButton(
        onClick = { gameplayViewModel.ForceBuy(recipe, amountToBuy) },
        enable = recipes.value.canBuy(recipe, amountToBuy, money.value.current),
        text = "Buy $amountToBuy (" + (recipes.value.GetNextCost(
            recipe,
            amountToBuy
        )).ValueAsString() + ")"
    )
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
        var name = stringResource(recipe.name)
        if (inSidebar) {
            name += " (" + recipeAmount.toString() + ")"
        }
        Text(
            fontSize = spBase,
            text = name,
            color = if (inSidebar) Color.Black else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        if (!inSidebar) {
            Text(
                fontSize = spBase * .8f,
                text = stringResource(
                    R.string.RecipeCountAndTotal, recipeAmount.toString(), stringResource(
                        R.string.money_per_cycle,
                        recipe.generating * recipeAmount
                    )
                ),
                color = if (inSidebar) Color.Black else Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
    var padding: Dp = 0.dp
    if (inSidebar) padding = 5.dp else padding = 50.dp
    Image(
        painter = painterResource(recipe.imageResourceId),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
			.fillMaxWidth()
			.padding(padding),
        contentDescription = null
    )
    if (showName && !inSidebar) {
        PerSecondText(recipe.generating, spBase * .8f, modifier.fillMaxWidth())
    }
}

@Preview(widthDp = 800, heightDp = 2400)
@Composable
fun CurrentRecipe_P() {
    val gameplayViewModel = GameplayViewModel()
    CurrentRecipe(recipeID = 0, gameState = gameplayViewModel)
}