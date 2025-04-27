package com.example.shaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.allRecipes
import com.example.shaker.home.Accelerometer
import com.example.shaker.home.CenterSidebarPager
import com.example.shaker.home.ShakeListener
import com.example.shaker.ui.GameplayViewModel
import com.example.shaker.ui.MainViewModel
import com.example.shaker.ui.gameplayStates.AdvancementState
import com.example.shaker.ui.gameplayStates.MoneyState
import com.example.shaker.ui.gameplayStates.RecipeState
import com.example.shaker.ui.gameplayStates.UpgradeState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()
	private var gameplayState: GameplayViewModel =
		GameplayViewModel(
			0,
			false
		) //Overriden by what's stored in the preferences and the default load, just used in case of reset
	private val sensor: Accelerometer = Accelerometer()

	@SuppressLint("SourceLockedOrientationActivity", "NewApi")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		loadPreferences()
		val shakeDelay: Long = resources.getInteger(R.integer.shakeDelayMilli).toLong()
		sensor.initialize(
			context = this,
			listener = ShakeListener(
				shakeMin = resources.getInteger(R.integer.minShakeIntensity).toFloat()
			) { intensity ->
				gameplayState.onShaked(shakeDelay)
			})
		//Check haptic enabled
		val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
		val vibrator = vibratorManager.defaultVibrator
		val delay: Long = resources.getInteger(R.integer.shakeDelayMilli).toLong()
		setContent {
			LocalActivity.current?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

			AppTheme(darkTheme = false, dynamicColor = false) {
				CenterSidebarPager(viewModel, gameplayState, 0)
				LaunchedEffect(Unit) {
					val valueIncrement = resources.getInteger(R.integer.NumberOfValueIncrementsPerS)
					val duration: Float =
						resources.getInteger(R.integer.CycleDurationMultiplier).toFloat()
					val cycleDuration = 1000L / valueIncrement
					while (true) {
						delay(cycleDuration)
						gameplayState.increment(1.0f / (duration * valueIncrement))
					}
				}
				val adv = gameplayState.advancementState.collectAsState().value
				if (adv.getAdvancement("shaking")) {
					gameplayState.toggleAdvancement("shaking")
					if (!adv.getAdvancement("firstBuy")) {
						val money = gameplayState.moneyState.collectAsState().value
						val mon = money.current.toInt()
						if (mon <= 15) {
							vibrator.vibrate(
								android.os.VibrationEffect.createOneShot(
									(integerResource(R.integer.shakeDelayMilli) / 2).toLong(),
									if (mon == 15) 255 else android.os.VibrationEffect.EFFECT_TICK
								)
							)
						}
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		sensor.subscribe()
	}

	override fun onPause() {
		super.onPause()
		sensor.unSubscribe()
	}

	override fun onStop() {
		super.onStop()
		savePreferences()
	}

	fun savePreferences() {
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

		with(sharedPref.edit()) {
			putLong(getString(R.string.lastTime), System.currentTimeMillis())
			apply()
		}
	}

	fun loadPreferences() {
		val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
		val current = sharedPref.getString(getString(R.string.current), null)
		val perShake = sharedPref.getString(getString(R.string.perShake), null)
		val perSecond = sharedPref.getString(getString(R.string.perSecond), null)
		if (current == null || perShake == null || perSecond == null) {
			return
		}
		val moneyState =
			MoneyState(ScalingInt(current), ScalingInt(perSecond), ScalingInt(perShake))


		val map: Map<Int, Long> =
			allRecipes.associate { rec ->
				rec.id to sharedPref.getLong(getString(R.string.recipe, rec.id), 0L)
			}
		val recipeState = RecipeState(map)

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

		val lastTime = sharedPref.getLong(getString(R.string.lastTime), System.currentTimeMillis())
		val elapsed = System.currentTimeMillis() - lastTime
		if (elapsed > 1000L) {
			gameplayState.increment(elapsed / (1000f * resources.getInteger(R.integer.CycleDurationMultiplier)))
		}

	}

}

@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewLight() {
	val viewModel = MainViewModel()
	val gameplayState = GameplayViewModel()
	gameplayState.increment(999f)
	gameplayState.increment(2f)
	AppTheme(darkTheme = false, dynamicColor = false) {
		CenterSidebarPager(viewModel, gameplayState)
	}
}

@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewRecipesLight() {
	val viewModel = MainViewModel()
	val gameplayState = GameplayViewModel()
	gameplayState.increment(999f)
	gameplayState.increment(2f)
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
	gameplayState.increment(999f)
	gameplayState.increment(2f)
	AppTheme(darkTheme = false, dynamicColor = false) {
		CenterSidebarPager(viewModel, gameplayState, 1)
	}
}


@Preview(widthDp = 380, heightDp = 680)
@Composable
fun AppPreviewDark() {
	val viewModel = MainViewModel()
	val gameplayState = GameplayViewModel()
	gameplayState.increment(999f)
	gameplayState.increment(2f)
	AppTheme(darkTheme = true, dynamicColor = false) {
		CenterSidebarPager(viewModel, gameplayState)
	}
}

data class TabItem(val name: String, val screen: String)