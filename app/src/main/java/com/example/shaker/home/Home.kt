@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shaker.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.shaker.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.ui.theme.ShakerTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    val mod = modifier.fillMaxWidth()
    Scaffold(topBar = {
        TopBar(
            currentMoney = "123292I292930",
            moneyPerS = "1628",
            modifier = mod
        )
    }) { ip ->
        Column(
            modifier = mod.padding(ip),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShakerImage(modifier = mod)
            MoneyText(
                str = R.string.money_on_shake,
                value = "12",
                size = 9.sp,
                modifier = mod
            )
        }
    }
}

@Composable
fun MoneyText(@StringRes str: Int, value: String, size: TextUnit, modifier: Modifier) {
    Text(
        text = stringResource(str, value),
        fontSize = size,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TopBar(currentMoney: String, moneyPerS: String, modifier: Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally) {
                MoneyText(
                    str = R.string.current_money,
                    value = currentMoney,
                    size = 24.sp,
                    modifier = modifier
                )
                MoneyText(
                    str = R.string.money_per_s,
                    value = moneyPerS,
                    size = 16.sp,
                    modifier = modifier
                )
            }}, modifier = Modifier)
}

@Composable
fun ShakerImage(rotation: Float = 22f, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.placeholder),
        alignment = Alignment.Center,
        contentScale = ContentScale.Inside,
        modifier = modifier
            .padding(all = 20.dp)
            .rotate(rotation),
        contentDescription = null
    )
}

@Preview
@Composable
fun HomePagePreview() {
    ShakerTheme {
        Surface {
            HomePage()
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    Surface(Modifier.fillMaxWidth()) {
        TopBar("1628299", "716", Modifier)
    }
}