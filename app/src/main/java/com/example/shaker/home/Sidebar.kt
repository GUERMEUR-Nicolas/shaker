package com.example.shaker.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shaker.R
import com.example.shaker.data.Upgrade
import com.example.shaker.data.upgrades


@Composable
fun Sidebar(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            UpgradeMainIcon()
        }
    )
    { pad ->
        LazyColumn(contentPadding = pad) {
            items(upgrades) { item ->
                UpgradeItem(item)
            }
        }
    }

}

@Composable
private fun UpgradeMainIcon(modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.upgrade_name), textAlign = TextAlign.Center)
        UpgradeItem(Upgrade(R.drawable.placeholder, R.string.upgrade_name), modifier)
    }
}

@Composable
private fun UpgradeItem(upgrade: Upgrade, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(upgrade.imageResourceId),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            //.fillMaxSize()
            .padding(all = 75.dp)
            .graphicsLayer(transformOrigin = TransformOrigin.Center),
        contentDescription = null
    )
}

@Preview
@Composable
private fun IconPreview() {
    UpgradeItem(upgrades[0])
}


@Preview
@Composable
private fun SidebarPreview() {
    Sidebar()
}