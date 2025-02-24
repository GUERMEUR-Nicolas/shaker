package com.example.shaker.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Upgrade(modifier: Modifier = Modifier) {
	val fillH = modifier.fillMaxHeight()
	Surface(){
		Row(){
			//Sidebar()
			CurrentUpgrade()
		}
	}
}

@Composable
fun CurrentUpgrade(modifier: Modifier = Modifier){
	Box(modifier
		.fillMaxSize()
	){
		Text("Upgrades")
	}
}

@Preview
@Composable
fun Upgrade_P(){
	Upgrade()
}