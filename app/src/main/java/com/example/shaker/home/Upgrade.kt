package com.example.shaker.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
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
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allUpgrades

@Composable
fun Upgrade(upg: Upgrade, modifier: Modifier = Modifier) {
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
			var inner = modifier
				.align(Alignment.Center)
			Text(
				text = upg.tier.toString(),
				fontSize = 16.sp,
				textAlign = TextAlign.Right,
				modifier = inner
					.align(Alignment.TopStart)
					.padding(bottom = 50.dp, start = 50.dp, end = 5.dp)
			)
			Text(
				text = stringResource(upg.name),
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
}

@Composable
@Preview(widthDp = 100, heightDp = 1000)
fun UpgradePreview() {
    Upgrade(allUpgrades[2], Modifier.size(75.dp))
}

@Composable
@Preview(widthDp = 100, heightDp = 1000)
fun UpgradePreview2() {
    Upgrade(allUpgrades[2], Modifier.size(100.dp))
}
