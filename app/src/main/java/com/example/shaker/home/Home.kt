@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shaker.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.shaker.ui.theme.ShakerTheme

@Composable
fun Home(modifier: Modifier = Modifier) {
	val fillH = modifier.fillMaxHeight()
	Surface(modifier = modifier.fillMaxSize()/*.background(Color.Gray)*/) {
		Row(verticalAlignment = Alignment.Top,
			horizontalArrangement = Arrangement.SpaceAround,
			modifier = modifier.fillMaxSize()/*.background(Color.Green)*/) {
			MainContent(Modifier)
			//Sidebar(modifier = fillH.weight(1f,false))
		}
	}
}


@Composable
private fun MainContent(modifier: Modifier = Modifier) {
	Column(
		modifier =  modifier
			.padding(vertical = 50.dp)/*.background(Color.Blue)*/,
		verticalArrangement = Arrangement.SpaceBetween,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = "SHAKER",
			fontSize = 48.sp,
			modifier = Modifier.weight(.5f),
			textAlign = TextAlign.Center)
		TopBar(
			currentMoney = "123292I292930",
			moneyPerS = "1628",
			modifier = Modifier.weight(1f).padding(horizontal = 10.dp)/*.background(Color.Yellow)*/.fillMaxWidth()
		)
		ShakerImage(modifier = Modifier.weight(3f))
		MoneyText(
			str = R.string.money_on_shake,
			value = "12",
			size = 16.sp,
			modifier = Modifier.weight(1f).padding(top = 10.dp)//.fillMaxWidth()
		)
	}
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
fun TopBar(currentMoney: String, moneyPerS: String, modifier: Modifier) {
	Column(
		modifier = modifier.padding(all = 10.dp)/*.background(Color.Red)*/,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Bottom
	) {
		MoneyText(
			str = R.string.current_money,
			value = currentMoney,
			size = 30.sp,
			modifier = Modifier
		)
		MoneyText(
			str = R.string.money_per_s,
			value = moneyPerS,
			size = 20.sp,
			modifier = Modifier
		)
	}
}

@Composable
fun ShakerImage(rotation: Float = 22f, modifier: Modifier = Modifier) {
	Image(
		painter = painterResource(R.drawable.placeholder),
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
		Home()
	}
}

@Preview
@Composable
fun TopBarPreview() {
	Surface(Modifier.fillMaxWidth()) {
		TopBar("1628299", "716", Modifier)
	}
}