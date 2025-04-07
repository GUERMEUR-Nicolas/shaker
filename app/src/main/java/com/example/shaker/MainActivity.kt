package com.example.shaker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shaker.home.MainViewModel
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.shaker.data.allRecipes
import com.example.shaker.home.CenterSidebarPager
import com.example.shaker.home.Accelerometer
import com.example.shaker.home.ShakeListener
import com.example.shaker.ui.GameplayViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val gameplayState: GameplayViewModel by viewModels()
    private val sensor: Accelerometer = Accelerometer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shakeDelay: Long = resources.getInteger(R.integer.shakeDelayMilli).toLong()
        sensor.Initialize(
            context = this,
            listener = ShakeListener(
                shakeMin = resources.getInteger(R.integer.minShakeIntensity).toFloat()
            ) { intensity ->
                //TODO integrate intensity
                gameplayState.OnShaked(shakeDelay)
            })
        //enableEdgeToEdge()
        val delay: Long = resources.getInteger(R.integer.shakeDelayMilli).toLong()
        setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                CenterSidebarPager(viewModel, gameplayState)
                LaunchedEffect(Unit) {
                    val valueIncrement = resources.getInteger(R.integer.ValueIncrementPerS)
                    val duration: Float =
                        resources.getInteger(R.integer.CycleDurationMultiplier).toFloat()
                    val cycleDuration = 1000L / valueIncrement
                    while (true) {
                        delay(cycleDuration)
                        gameplayState.Increment(1.0f / (duration * valueIncrement))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensor.subscribe()
    }

    //TODO, don't we ACTUALLY want to also detect shake when apply is paused/out of focus ????
    override fun onPause() {
        super.onPause()
        sensor.unSubscribe()
    }

}

@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewLight() {
    val viewModel = MainViewModel()
    val gameplayState = GameplayViewModel()
    gameplayState.Increment(999f)
    gameplayState.Increment(2f)
    AppTheme(darkTheme = false, dynamicColor = false) {
        CenterSidebarPager(viewModel, gameplayState)
    }
}
@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewRecipesLight() {
    val viewModel = MainViewModel()
    val gameplayState = GameplayViewModel()
    gameplayState.Increment(999f)
    gameplayState.Increment(2f)
    AppTheme(darkTheme = false, dynamicColor = false) {
        CenterSidebarPager(viewModel, gameplayState, 1)
    }
}
@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewClickedUpgradeLight() {
    val viewModel = MainViewModel()
    viewModel.selectUpgrade(allRecipes[0].upgrades[0])
    val gameplayState = GameplayViewModel()
    gameplayState.Increment(999f)
    gameplayState.Increment(2f)
    AppTheme(darkTheme = false, dynamicColor = false) {
        CenterSidebarPager(viewModel, gameplayState, 1)
    }
}


@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewDark() {
    val viewModel = MainViewModel()
    val gameplayState = GameplayViewModel()
    gameplayState.Increment(999f)
    gameplayState.Increment(2f)
    AppTheme(darkTheme = true,dynamicColor = false) {
        CenterSidebarPager(viewModel, gameplayState)
    }
}

data class TabItem(val name: String, val screen: String)