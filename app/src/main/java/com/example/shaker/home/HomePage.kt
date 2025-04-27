package com.example.shaker.home

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.shaker.R
import com.example.shaker.data.ScalingInt
import com.example.shaker.ui.GameplayViewModel
import com.example.shaker.ui.theme.bodyFontFamily

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomePage(modifier: Modifier = Modifier, gameplayState: GameplayViewModel) {
	val st =
		if (gameplayState.moneyState.collectAsState().value.current.getExponent() >= 3) 1 else 0
	val bgs = arrayOf(R.drawable.p0j, R.drawable.p1j)
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
			TextButton(
				text = "x10",
				colors = ButtonDefaults.textButtonColors(
					containerColor = MaterialTheme.colorScheme.secondary,
					contentColor = MaterialTheme.colorScheme.onSecondary,
				),
				onClick = { gameplayState.timesTen() },
				enable = true,
			)
			MoneyOnShake(
				str = R.string.money_on_shake,
				Color.White,
				gameplayState,
				size = 16.sp,
				modifier = Modifier
					.weight(1f)
					.padding(top = 50.dp)
			)
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
		gameplayState.moneyState.collectAsState().value.perShake.valueAsString(),
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
	val mult = 1f / coef
	MoneyText(
		str = R.string.money_per_cycle,
		color,
		//Ensures we have correct potential floating point string displayed or floored exponential value
		value = if (perSecond.toInt() < coef) String.format(
			"%.1f",
			perSecond.toFloat() * mult
		) else (perSecond * mult).toString(),
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
			.fillMaxWidth()
			.graphicsLayer(transformOrigin = TransformOrigin.Center)
	)
}


@Preview(widthDp = 480)
@Composable
fun HomePagePreview() {
	AppTheme {
		val gameplayState = GameplayViewModel()
		HomePage(gameplayState = gameplayState)
	}
}