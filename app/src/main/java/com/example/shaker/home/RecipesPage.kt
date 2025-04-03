package com.example.shaker.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
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
        CurrentRecipe(recipeID = id, gameState, viewModel = viewModel)
    }
}

@Composable
fun CurrentRecipe(
    recipeID: Int,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val recipe = allRecipes[recipeID]
    Box(modifier.fillMaxSize()) {
        val selectedUpgrade = viewModel.selectedUpgrade.collectAsState().value
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
        ) {
            RecipeInfo(recipe, gameState = gameState, 40.sp, true, false, modifier) {
                if (selectedUpgrade is Upgrade) {
                    //Spacer(modifier.height(15.dp))
                    UpgradePanel(
                        recipe,
                        selectedUpgrade,
                        { viewModel.selectUpgrade(null) },
                        modifier
                            .fillMaxWidth(0.8f),
                        //.align(Alignment.CenterEnd),
                        gameState
                    )
                    //Spacer(modifier.height(15.dp))
                } else {
                    CenteredImage(recipe.imageResourceId, 50.dp)
                }
            }
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
            UpgradeRow(recipe, modifier, 75.dp, gameState, viewModel)
        }

    }
}

@Composable
fun UpgradeRow(
    recipe: Recipe,
    modifier: Modifier,
    dp: Dp,
    gameState: GameplayViewModel,
    viewModel: MainViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        //TODO fetch the actual upgrades of the recipe
        for (i in recipe.upgrades) {
            UpgradeWithButton(recipe, i.first, modifier, dp, gameState, viewModel)
        }
    }
}

@Composable
fun UpgradeWithButton(
    recipe: Recipe,
    upgrade: Upgrade,
    modifier: Modifier,
    dp: Dp,
    gameState: GameplayViewModel,
    viewModel: MainViewModel
) {
    //TODO
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        UpgradeIcon(upgrade, Modifier
            .size(dp)
            .clickable() {
                viewModel.selectUpgrade(upgrade)
            })
        //UpgradeBuyButton(recipe, upgrade, modifier, gameState)
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
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    val recipes = gameState.recipes.collectAsState()
    val recipeAmount = recipes.value.GetRecipeAmount(recipe)
    var name = stringResource(recipe.name)
    if (inSidebar) {
        name += " (" + recipeAmount.toString() + ")"
    }
    TitledElement(
        name,
        if (!inSidebar) stringResource(
            R.string.RecipeCountAndTotal, recipeAmount.toString(), stringResource(
                R.string.money_per_cycle,
                recipe.generating * recipeAmount
            )
        ) else null,
        if (inSidebar) Color.Black else Color.White,
        spBase
    ) {
        content()
    }
    if (showName && !inSidebar) {
        PerSecondText(recipe.generating, spBase * .8f, modifier.fillMaxWidth())
    }
}

@Composable
fun TitledImage(
    title: String,
    subTitle: String?,
    textColor: Color,
    titleSize: TextUnit,
    @DrawableRes imageId: Int,
    imagePadding: Dp,
    modifier: Modifier = Modifier,
) {
    TitledElement(title, subTitle, textColor, titleSize, modifier) {
        CenteredImage(imageId, imagePadding)
    }
}

@Composable
fun CenteredImage(imageId: Int, imagePadding: Dp) {
    Image(
        painter = painterResource(imageId),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .padding(imagePadding),
        contentDescription = null
    )
}

@Composable
fun TitledElement(
    title: String,
    subTitle: String?,
    textColor: Color,
    titleSize: TextUnit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Text(
        fontSize = titleSize,
        text = title,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    if (subTitle is String && !subTitle.isNullOrEmpty()) {
        Text(
            fontSize = titleSize * .8f,
            text = subTitle,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    content()
}

@Preview(widthDp = 800, heightDp = 2400)
@Composable
fun CurrentRecipe_P() {
    val gameplayViewModel = GameplayViewModel()
    CurrentRecipe(recipeID = 0, gameState = gameplayViewModel, viewModel = MainViewModel())
}

@Preview(widthDp = 400, heightDp = 400)
@Composable
fun CurrentRecipe_SideBar() {
    val gameplayViewModel = GameplayViewModel()
    RecipeItem(
        recipe = allRecipes[0],
        isSelected = true,
        gameState = gameplayViewModel,
        onRecipeClick = {},
        modifier = Modifier
    )
}