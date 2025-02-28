package com.example.shaker.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Upgrade
import com.example.shaker.data.upgrades
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovingSideBar(
    viewModel: MainViewModel,
    pagerState_H: PagerState,
    pagerState_V: PagerState,
    modifier: Modifier
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var sidebarWidthPx by remember { mutableStateOf(0) }
    val sidebarWidthDp = with(density) { sidebarWidthPx.toDp() }

    val offset =
        -(configuration.screenWidthDp.dp - sidebarWidthDp) * (pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction)

    val coroutineScope = rememberCoroutineScope() // scroll to page
    val selectedUpgradeId by viewModel.selectedUpgradeId.collectAsState()
    UpgradeSidebar(
        selectedUpgradeId = selectedUpgradeId,
        onUpgradeClick = { upgradeId ->
            viewModel.selectUpgrade(upgradeId)
            coroutineScope.launch {
                pagerState_V.animateScrollToPage(upgradeId)
            }
        },
        modifier = modifier
			.fillMaxWidth(.2f)
			.onGloballyPositioned { coordinates ->
				sidebarWidthPx = coordinates.size.width
			}
			.offset { IntOffset(offset.roundToPx(), 0) }
    )
}

@Composable
fun UpgradeSidebar(
    selectedUpgradeId: Int,
    onUpgradeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        UpgradeMainIcon(Modifier)
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                userScrollEnabled = true,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(all = 5.dp)
            ) {
                items(upgrades) { upgrade ->
                    UpgradeItem(
                        upgrade = upgrade,
                        isSelected = upgrade.id == selectedUpgradeId,
                        onUpgradeClick = onUpgradeClick,
                        Modifier
							.padding(vertical = 1.dp)
							.fillMaxWidth()
                    )
                }
            }
        }
    }

}

@Composable
private fun UpgradeMainIcon(modifier: Modifier = Modifier) {
    Surface(
        modifier
			.fillMaxWidth()
			.background(Color.Red)
    ) {
        Text(
            text = stringResource(R.string.upgrade_name).substring(0, 2).uppercase(),
            textAlign = TextAlign.Center,
            fontSize = 45.sp,
            modifier = modifier
                .padding(horizontal = 5.dp)
        )
    }
}

@Composable
private fun UpgradeItem(
    upgrade: Upgrade,
    isSelected: Boolean,
    onUpgradeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showName: Boolean = true
) {
    Column(
        modifier = modifier
			.clickable { onUpgradeClick(upgrade.id) }
			.background(if (isSelected) Color.Green else Color.White),
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

/*@Preview
@Composable
private fun IconPreview() {
	UpgradeItem(upgrades[0], { })
}*/


/*@Preview(widthDp = 70)
@Composable
private fun SidebarPreview() {
	Sidebar({}, 0, Modifier.fillMaxHeight())
}*/