package com.example.shaker.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.shaker.data.allUpgrades
import com.example.shaker.ui.GameplayViewModel

@Composable
fun UpgradePanel(
    recipe: Recipe,
    upg: Upgrade,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    backColor : Color,
    color : Color,
    gameState: GameplayViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth(1f)
            .background(backColor)
            //align(Alignment.CenterEnd),
    ) {
        //Box(
        //    modifier = Modifier
        //        .fillMaxWidth()
        //        .background(Color.Red)
        //        .padding(10.dp)
        //        .padding(bottom = 10.dp)
        //        .clickable(onClick = onExit)
        //) {
        //}
        TitledImage(
            title = stringResource(upg.name) + "\n" +stringResource(R.string.tier_name,upg.tier.level),
            subTitle = stringResource(upg.description),
            textColor = color,
            titleSize = 30.sp,
            imageId = upg.image,
            imagePadding = 50.dp,
        )
        UpgradeBuyButton(recipe, upg, gameState, modifier)
    }
}

@Composable
fun UpgradeIcon(upg: Upgrade, modifier: Modifier = Modifier) {
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
        var inner = modifier
            .align(Alignment.Center)
        Text(
            text = upg.tier.level.toString(),
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
            //.background(Color.Blue)
            //.fillMaxWidth()
            //.background(
            //    colorResource(R.color.textBg)
            //)
        )
    }
}

@Composable
fun UpgradeBuyButton(
    recipe: Recipe,
    upgrade: Upgrade,
    gameplayViewModel: GameplayViewModel,
    modifier: Modifier
) {
    //TODO bind viewModel/money and enable and procesed the onClick and enabled with money dedeuciton and stuff like in the recipeBuyButton
    GenericBuyButton(
        //TODO
        onClick = { gameplayViewModel.ForceBuy(recipe, 0) },
        enable = true,
        text = stringResource(
            R.string.buy_upgrade,
            stringResource(R.string.money_value, upgrade.cost)
        )
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
        RecipeInfo(allRecipes[0], GameplayViewModel(), 12.sp, true, false, Modifier,MaterialTheme.colorScheme.onTertiaryContainer, MaterialTheme.colorScheme.tertiaryContainer) {
            //UpgradePanel(
            //    allRecipes[0],
            //    allUpgrades[0],
            //    {},
            //    Modifier.fillMaxSize(),
            //    GameplayViewModel()
            //)
        }
    }
}

@Composable
@Preview(widthDp = 100, heightDp = 1000)
fun UpgradePreview() {
    UpgradeIcon(allUpgrades[0], Modifier.size(75.dp))
}

