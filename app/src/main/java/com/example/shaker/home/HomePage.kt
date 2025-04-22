@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shaker.home

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.shaker.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.shaker.data.ScalingInt
import com.example.shaker.ui.GameplayViewModel
import com.example.ui.theme.bodyFontFamily
import kotlin.math.abs
import kotlin.math.pow

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomePage(modifier: Modifier = Modifier, gameplayState: GameplayViewModel) {
    val st =
        if (gameplayState.moneyState.collectAsState().value.current.getExponent() >= 3) 1 else 0
    val bgs = when {
        /*isSystemInDarkTheme()*/false -> arrayOf(
            R.drawable.p0n,
            R.drawable.p1n
        ) // TODO: use darkTheme from AppTheme
        else -> arrayOf(R.drawable.p0j, R.drawable.p1j)
    }
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
        )
        Image(
            painter = painterResource(id = bgs[st]),
            contentDescription = "background",
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = modifier
                .padding(vertical = 50.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                gameplayState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            )
            ShakerImage(
                modifier = Modifier
                    .weight(3f)
                    .padding(5.dp)
            )
//            TextButton(
//            //modifier = modifier.width(50.dp)
//                text = "x10",
//                colors = ButtonDefaults.textButtonColors(
//                    containerColor = MaterialTheme.colorScheme.secondary,
//                    contentColor = MaterialTheme.colorScheme.onSecondary,
//                ),
//                onClick = { gameplayState.TimesTen() },
//                enable = true,
//            )
//            MoneyOnShake(
//                str = R.string.money_on_shake,
//                gameplayState,
//                size = 16.sp,
//                modifier = Modifier
//					.weight(1f)
//					.padding(top = 50.dp)
//            )
        }
    }
}

@Composable
fun MoneyOnShake(
    @StringRes str: Int,
    color: Color,
    gameplayState: GameplayViewModel,
    size: TextUnit,
    modifier: Modifier
) {
    MoneyText(
        str,
        color,
        gameplayState.moneyState.collectAsState().value.perShake.ValueAsString(),
        size,
        modifier
    )
}

@Composable
fun MoneyText(
    @StringRes str: Int,
    color: Color,
    value: String,
    size: TextUnit,
    modifier: Modifier
) {
    Text(
        text = stringResource(str, value),
        style = TextStyle(fontSize = size, color = color, fontFamily = bodyFontFamily),
        textAlign = TextAlign.Center,
        modifier = modifier
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
//        MoneyText(
//            str = R.string.current_money,
//            value = state.value.current.ValueAsString(),
//            size = 30.sp,
//            modifier = Modifier
//        )
        FlipClockCounter(
            state,
            modifier,
            MaterialTheme.colorScheme.surfaceDim,
            MaterialTheme.colorScheme.onSurface
        )
        PerSecondText(
            state.value.perSecond,
            Color.White,
            20.sp,
            Modifier
        )
    }
}

@Composable
fun PerSecondText(perSecond: ScalingInt, color: Color, sp: TextUnit, modifier: Modifier) {
    val coef = integerResource(R.integer.CycleDurationMultiplier)
    val mult = 1f / coef;
    MoneyText(
        str = R.string.money_per_cycle,
        color,
        //Ensures we have correct potential floating point string displayed or floored exponential value
        value = if (perSecond.toInt() < coef) String.format("%.1f", perSecond.toFloat() * mult) else (perSecond * mult).toString(),
        size = sp,
        modifier = modifier
    )
}

@Composable
fun ShakerImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.shaker),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillHeight,
        contentDescription = null,
        modifier = modifier
            //.padding(all = 20.dp)
            .fillMaxWidth()
            .graphicsLayer(transformOrigin = TransformOrigin.Center)
        //.rotate(rotation)/*.background(Color.Black)*/
    )
}

class BackLines(private val isInner: Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): androidx.compose.ui.graphics.Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                val md = if (isInner) 1f / 2f else 1f
                for (i in 0..if (isInner) 0 else 1) {
                    val stepsX = arrayOf(
                        i * size.width,
                        size.width * abs(x = i - (1f / 5f) * md),
                        (3.0 / md - 1).pow(i).toFloat() * size.width * md / 3f
                    )
                    val stepsY = arrayOf(
                        size.height * (md - 1) * -2f / 5f,
                        size.height / 3f,
                        if (isInner) size.height * (md - 1) * -8f / 5f else size.height
                    )
                    moveTo(stepsX[0], stepsY[0] - 100f)
                    lineTo(stepsX[1], stepsY[0])
                    lineTo(stepsX[2], stepsY[1])
                    lineTo(stepsX[2], 2 * stepsY[1])
                    lineTo(stepsX[1], stepsY[2])
                    lineTo(stepsX[0], stepsY[2] + 100f)
                }
            }
        )
    }
}


@Preview(widthDp = 480)
@Composable
fun HomePagePreview() {
    AppTheme {
        val gameplayState = GameplayViewModel()
        HomePage(gameplayState = gameplayState)
    }
}

//@Preview
//@Composable
//fun TopBarPreview() {
//    val gameplayState = GameplayViewModel()
//    Surface(Modifier.fillMaxWidth()) {
//        TopBar(gameplayState, Modifier)
//    }
//}
