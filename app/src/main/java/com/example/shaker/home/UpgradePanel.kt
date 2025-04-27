package com.example.shaker.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Recipe
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel

@Composable
fun UpgradePanel(
    recipe: Recipe,
    upg: Upgrade,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    backColor: Color,
    textColor: Color,
    gameState: GameplayViewModel
) {
    val upgradeLevels by gameState.upgradeLevels.collectAsState()
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth(1f)
            .background(backColor)
    ) {
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        TitledImage(
            title = stringResource(upg.name) + "\n" + stringResource(
                R.string.tier_name,
                upgradeLevels.getUpgradeLevel(recipe.id, upg.id)
            ),
            subTitle = upg.getDynamicDescription(),
            textColor = textColor,
            titleSize = 30.sp,
            imageId = upg.image,
            imagePadding = 50.dp,
        )
        UpgradeBuyButton(
            upg, { gameState.forceBuy(recipe, upg, 1) }, gameState,
            ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,
            )
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
fun UpgradeIcon(
    recipe: Recipe,
    upg: Upgrade,
    gameState: GameplayViewModel,
    modifier: Modifier = Modifier
) {
    val upgradeLevels by gameState.upgradeLevels.collectAsState()
    Box(modifier = modifier) {
        Box(
            modifier = modifier
                .background(colorResource(R.color.iconsBG))
                .padding(10.dp)
                .padding(bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(upg.image),
                contentDescription = null,
                modifier = modifier.align(Alignment.Center),
                contentScale = androidx.compose.ui.layout.ContentScale.FillHeight
            )
        }
        val inner = modifier
            .align(Alignment.Center)
        Text(
            text = upgradeLevels.getUpgradeLevel(recipe.id, upg.id).toString(),
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = inner
                .align(Alignment.TopStart)
                .padding(bottom = 50.dp, start = 50.dp, end = 5.dp)
        )
        Text(
            text = stringResource(upg.name),
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = inner
                .padding(top = 53.dp)
        )
    }
}

@Composable
fun UpgradeBuyButton(
    upgrade: Upgrade,
    onClick: () -> Unit,
    gameplayViewModel: GameplayViewModel,
    colors: ButtonColors
) {
    val recipes = gameplayViewModel.recipes.collectAsState()
    val money = gameplayViewModel.moneyState.collectAsState()
    val cost = recipes.value.getNextCost(upgrade, 1)
    TextButton(
        onClick = onClick,
        enable = recipes.value.canBuy(upgrade, 1, money.value.current),
        text = stringResource(
            R.string.buy_upgrade,
            stringResource(R.string.money_value, cost)
        ),
        colors = colors
    )
}

@Composable
@Preview(widthDp = 800, heightDp = 1500)
fun UpgradePanelPreview() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        RecipeInfo(
            recipe = allRecipes[0],
            gameState = GameplayViewModel(),
            spBase = 12.sp,
            showName = true,
            inSidebar = false,
            modifier = Modifier,
            fontColor = MaterialTheme.colorScheme.onTertiaryContainer,
            backColor = MaterialTheme.colorScheme.tertiaryContainer
        ) { }
    }
}