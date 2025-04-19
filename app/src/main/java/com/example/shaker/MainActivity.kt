package com.example.shaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.compose.AppTheme
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.allRecipes
import com.example.shaker.data.allUpgrades
import com.example.shaker.home.Accelerometer
import com.example.shaker.home.CenterSidebarPager
import com.example.shaker.home.MainViewModel
import com.example.shaker.home.ShakeListener
import com.example.shaker.ui.GameplayStates.AdvancementState
import com.example.shaker.ui.GameplayStates.MoneyState
import com.example.shaker.ui.GameplayStates.RecipeState
import com.example.shaker.ui.GameplayStates.UpgradeState
import com.example.shaker.ui.GameplayViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var gameplayState: GameplayViewModel =
        GameplayViewModel(10)//Overriden by what's stored in the preferences and the default loard, just uused in case of reset
    private val sensor: Accelerometer = Accelerometer()

    @SuppressLint("SourceLockedOrientationActivity", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val restart = false;
        if (restart) {
            savePreferencees()
        }
        loadPreferences();
        val shakeDelay: Long = resources.getInteger(R.integer.shakeDelayMilli).toLong()
        sensor.Initialize(
            context = this,
            listener = ShakeListener(
                shakeMin = resources.getInteger(R.integer.minShakeIntensity).toFloat()
            ) { intensity ->
                //TODO integrate intensity
                gameplayState.OnShaked(shakeDelay)
            })
        //Check haptic enabled
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        //Settings.System.getInt(requireContext().contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED) == 1
        //enableEdgeToEdge()
        val delay: Long = resources.getInteger(R.integer.shakeDelayMilli).toLong()
        setContent {
            LocalActivity.current?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            AppTheme(darkTheme = false, dynamicColor = false) {
                CenterSidebarPager(viewModel, gameplayState, 0)
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
                val adv = gameplayState.advancementState.collectAsState().value
                if (adv.getAdvancement("shaking")) {
                    gameplayState.toggleAdvancement("shaking")
                    if (!adv.getAdvancement("showSideBar")) {
                        vibrator.vibrate(
                            android.os.VibrationEffect.createOneShot(
                                (integerResource(R.integer.shakeDelayMilli) / 4).toLong(),
                                android.os.VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        );
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

    override fun onStop() {
        super.onStop()
        savePreferencees()
    }

    fun savePreferencees() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        //Money
        val money = gameplayState.moneyState.value
        with(sharedPref.edit()) {
            putString(getString(R.string.current), money.current.value.toString())
            putString(getString(R.string.perShake), money.perShake.value.toString())
            putString(getString(R.string.perSecond), money.perSecond.value.toString())
            apply()
        }
        //Recipes
        val recipes: RecipeState = gameplayState.recipes.value
        val map: Map<Int, Long> = recipes.map
        with(sharedPref.edit()) {
            for (entry in map) {
                putLong(getString(R.string.recipe, entry.key), entry.value)
            }
            apply()
        }
        //Upgrades
        val upgrades: Map<Pair<Int, Int>, Int> = gameplayState.upgradeLevels.value.map
        with(sharedPref.edit()) {
            for (entry in upgrades) {
                putInt(getString(R.string.upgrade, entry.key.first, entry.key.second), entry.value)
            }
            apply()
        }

        val adv = gameplayState.advancementState.value
        with(sharedPref.edit()) {
            for (entry in adv.map) {
                putBoolean(getString(R.string.advancement, entry.key), entry.value)
            }
            apply()
        }
    }

    fun loadPreferences() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val current = sharedPref.getString(getString(R.string.current), "10")!!
        val perShake = sharedPref.getString(getString(R.string.perShake), "1")!!
        val perSecond = sharedPref.getString(getString(R.string.perSecond), "0")!!
        val moneyState =
            MoneyState(ScalingInt(current), ScalingInt(perSecond), ScalingInt(perShake))

        val map: Map<Int, Long> =
            allRecipes.associate { rec ->
                rec.id to sharedPref.getLong(getString(R.string.recipe, rec.id), 0L)
            }
        val recipeState = RecipeState(map);

        val upgrades: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        for (recipe in allRecipes) {
            for (upgrade in recipe.upgrades) {
                val value = sharedPref.getInt(
                    getString(R.string.upgrade, recipe.id, upgrade.id),
                    0
                )
                upgrades[Pair(recipe.id, upgrade.id)] = value
            }
        }
        val upgradeState = UpgradeState(upgrades)

        val adv = mutableMapOf<String, Boolean>()
        //We use the default values of the map as the values we are looking for
        for (entry in gameplayState.advancementState.value.map.keys) {
            val value = sharedPref.getBoolean(getString(R.string.advancement, entry), false)
            adv[entry] = value
        }
        val advancementState = AdvancementState(adv)

        gameplayState = GameplayViewModel(
            moneyState,
            recipeState,
            upgradeState,
            advancementState
        )
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
    AppTheme(darkTheme = true, dynamicColor = false) {
        CenterSidebarPager(viewModel, gameplayState)
    }
}

data class TabItem(val name: String, val screen: String)