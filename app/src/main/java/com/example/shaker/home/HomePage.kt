@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shaker.home

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.shaker.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.data.ScalingInt
import com.example.shaker.ui.GameplayViewModel
import com.example.shaker.ui.theme.ShakerTheme

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomePage(modifier: Modifier = Modifier, gameplayState: GameplayViewModel) {
    Surface(modifier = modifier.fillMaxSize()/*.background(Color.Gray)*/) {
        Column(
            modifier = modifier
                .padding(vertical = 50.dp)/*.background(Color.Blue)*/,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SHAKER",
                fontSize = 48.sp,
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.Center
            )
            TopBar(
                gameplayState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)/*.background(Color.Yellow)*/
                    .fillMaxWidth()
            )
            ShakerImage(modifier = Modifier.weight(3f))
            MoneyOnShake(
                str = R.string.money_on_shake,
                gameplayState,
                size = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp)//.fillMaxWidth()
            )
        }
    }
}
@Composable
fun MoneyOnShake(@StringRes str: Int, gameplayState: GameplayViewModel, size: TextUnit, modifier: Modifier) {
    MoneyText(
        str, gameplayState.moneyState.collectAsState().value.perShake.ValueAsString(),size,modifier
    )
}
@Composable
fun MoneyText(@StringRes str: Int, value: String, size: TextUnit, modifier: Modifier) {
    Text(
        text = stringResource(str, value),
        fontSize = size,
        textAlign = TextAlign.Center,
        modifier = modifier/*.background(Color.Gray)*/
    )
}

@Composable
fun TopBar(gameplayState: GameplayViewModel, modifier: Modifier) {
    val state = gameplayState.moneyState.collectAsState()
    Column(
        modifier = modifier.padding(all = 10.dp)/*.background(Color.Red)*/,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        MoneyText(
            str = R.string.current_money,
            value = state.value.current.ValueAsString(),
            size = 30.sp,
            modifier = Modifier
        )
        PerSecondText(state.value.perSecond, 20.sp,Modifier)
    }
}

@Composable
fun PerSecondText(perSecond: ScalingInt, sp: TextUnit, modifier: Modifier) {
    MoneyText(
        str = R.string.money_per_s,
        value = perSecond.ValueAsString(),
        size = sp,
        modifier = modifier
    )
}

@Composable
fun ShakerImage(rotation: Float = 22f, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.placeholder_0),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = modifier
            .padding(all = 75.dp)
            .fillMaxWidth()
            .graphicsLayer(transformOrigin = TransformOrigin.Center)
            .rotate(rotation)/*.background(Color.Black)*/
    )
}

@Preview(widthDp = 480)
@Composable
fun HomePagePreview() {
    ShakerTheme {
        val gameplayState = GameplayViewModel()
        HomePage(gameplayState = gameplayState)
    }
}

@Preview
@Composable
fun TopBarPreview() {
    val gameplayState = GameplayViewModel()
    Surface(Modifier.fillMaxWidth()) {
        TopBar(gameplayState, Modifier)
    }
}