package com.example.shaker.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Upgrade
import com.example.shaker.data.upgrades


@Composable
fun Sidebar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier//.background(Color.Gray),
    )
    {
        UpgradeMainIcon(Modifier)
        Box(Modifier.fillMaxSize()/*.background(Color.Blue)*/) {
            LazyColumn(
                userScrollEnabled = false,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(all= 5.dp)//.background(Color.Red)//.fillMaxWidth()
            ) {
                items(upgrades) { item ->
                    UpgradeItem(
                        item, Modifier
                            .padding(vertical = 1.dp)
                            .fillMaxWidth()
                            //.background(Color.Blue)
                    )
                }
            }
        }
    }

}

@Composable
private fun UpgradeMainIcon(modifier: Modifier = Modifier) {
    Surface(modifier
        .fillMaxWidth()
        .background(Color.Red)) {
        Text(
            text = stringResource(R.string.upgrade_name).substring(0,2).uppercase(),
            textAlign = TextAlign.Center,
            fontSize = 45.sp,
            modifier = modifier
                .padding(horizontal = 5.dp)
                //.background(Color.Yellow)
        )
        //UpgradeItem(Upgrade(R.drawable.placeholder, R.string.upgrade_name), modifier, showName = false)
    }
}

@Composable
private fun UpgradeItem(upgrade: Upgrade, modifier: Modifier = Modifier, showName: Boolean = true) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        Image(
            painter = painterResource(upgrade.imageResourceId),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .padding(all = 5.dp)
                .graphicsLayer(transformOrigin = TransformOrigin.Center),
            contentDescription = null
        )
        if (showName) {
            Text(
                text = stringResource(upgrade.name),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                modifier = modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

@Preview
@Composable
private fun IconPreview() {
    UpgradeItem(upgrades[0])
}


@Preview(widthDp = 70)
@Composable
private fun SidebarPreview() {
    Sidebar(Modifier.fillMaxHeight())
}