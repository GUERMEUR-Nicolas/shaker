package com.example.shaker.ui

import androidx.lifecycle.ViewModel
import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allRecipes
import com.example.shaker.data.doAllUpgradesOfType
import com.example.shaker.ui.gameplayStates.AdvancementState
import com.example.shaker.ui.gameplayStates.MoneyState
import com.example.shaker.ui.gameplayStates.RecipeState
import com.example.shaker.ui.gameplayStates.UpgradeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameplayViewModel(
	private val _moneyState: MutableStateFlow<MoneyState>,
	private val _recipes: MutableStateFlow<RecipeState>,
	private val _upgradeLevels: MutableStateFlow<UpgradeState>,
	private var _advancementState: MutableStateFlow<AdvancementState>


) : ViewModel() {

	constructor(
		moneyState: MoneyState,
		recipeState: RecipeState,
		upgradeState: UpgradeState,
		advancementState: AdvancementState
	) : this(
		MutableStateFlow<MoneyState>(moneyState),
		MutableStateFlow(recipeState),
		MutableStateFlow(upgradeState),
		MutableStateFlow(advancementState)
	)

	constructor(init: Int = 10, sidebar: Boolean = false, firstBuy: Boolean = false) : this(
		MoneyState(ScalingInt(init)),
		RecipeState(),
		UpgradeState(),
		AdvancementState()
	) {
		if (sidebar)
			toggleAdvancement("showSideBar")
		if (firstBuy)
			toggleAdvancement("firstBuy")
	}

	val moneyState: StateFlow<MoneyState> = _moneyState.asStateFlow()
	val recipes: StateFlow<RecipeState> = _recipes.asStateFlow()

	val upgradeLevels: StateFlow<UpgradeState> = _upgradeLevels.asStateFlow()
	var accumulated: Float = 0f
	val advancementState: StateFlow<AdvancementState> = _advancementState.asStateFlow()


	fun sell(recipe: Recipe, amount: Long) {
		forceBuy(recipe, -amount)
	}


	fun forceBuy(recipe: Recipe, amount: Long) {
		val id = recipe.id
		_moneyState.value = _moneyState.value.copy(
			previous = _moneyState.value.current,
			current = _moneyState.value.current - recipe.getNextCost(
				_recipes.value.getRecipeAmount(
					recipe
				), amount
			)
		)
		//We increment the recipe count after we effectively bought it, other ways, we'd pay one level ahead
		_recipes.value = _recipes.value.copy(
			map = _recipes.value.incrementedMap(id, amount)
		)
		this.updatePerSecond()
	}

	fun forceBuy(recipe: Recipe, upgrade: Upgrade, amount: Long = 1) {
		_moneyState.value = _moneyState.value.copy(
			previous = _moneyState.value.current,
			current = _moneyState.value.current - upgrade.getNextCost(amount)
		)
		_upgradeLevels.value = _upgradeLevels.value.copy(
			map = _upgradeLevels.value.incrementedMap(Pair(recipe.id, upgrade.id), amount.toInt())
		)
		upgrade.level += amount.toInt()
		this.updatePerSecond()
		this.updatePerShake()
	}

	fun increment(value: ScalingInt) {
		_moneyState.value = _moneyState.value.copy(
			previous = _moneyState.value.current,
			current = _moneyState.value.current + value
		)
		if (!_advancementState.value.getAdvancement("showSideBar")
			&& _recipes.value.canBuy(allRecipes[0], 1, _moneyState.value.current)
		) {
			toggleAdvancement("showSideBar")
		}
		if (_recipes.value.amountDiscovered < allRecipes.size) {
			for (i in allRecipes.indices) {
				if (!allRecipes[i].isDiscovered && (_moneyState.value.current >= allRecipes[i].cost.basePrice * 0.5)) {
					allRecipes[i].isDiscovered = true
					_recipes.value = _recipes.value.copy(
						amountDiscovered = _recipes.value.incrementDiscovered()
					)
				}
			}
		}
	}

	fun increment(numberOfCycle: Float) {
		val incr = numberOfCycle * _moneyState.value.perSecond.toFloat()
		//If the value is directly above the minimal value, we increment it directly, using the ScalingInt Multiplication
		if (incr > 1000f) {
			increment(ScalingInt(incr))
		} else {
			//Else, mostly in the beginning when incr is less than 1 per update rate, we accumulate it in a float value
			accumulated += incr
			if (accumulated > 1f) {
				//When the value is above 1, we increment the whole value cropped (maybe we accumulated from 0.99 to 1929.97 and keep the rest for the next cycle
				val wholeAcc = accumulated.toInt()
				increment(ScalingInt(wholeAcc))
				accumulated -= wholeAcc
			}
		}
	}

	var lastShake: Long = 0L
	fun onShaked(delay: Long) {
		val current = System.currentTimeMillis()
		if ((current - lastShake) > delay) {
			this.toggleAdvancement("shaking")
			lastShake = current
			increment(_moneyState.value.perShake)
		}
	}

	fun timesTen() {
		_moneyState.value = _moneyState.value.copy(
			previous = _moneyState.value.current,
			current = _moneyState.value.current * 10
		)
	}

	private fun updatePerSecond() {
		var perSecond = ScalingInt(0)
		for (r in allRecipes)
			perSecond += doAllUpgradesOfType(
				r.upgrades,
				r.generating,
				"generate",
				_recipes.value.map
			) * _recipes.value.getRecipeAmount(r)
		_moneyState.value = _moneyState.value.copy(
			perSecond = perSecond
		)
	}

	private fun updatePerShake() {
		var perShake = ScalingInt(1)
		for (r in allRecipes)
			perShake *= doAllUpgradesOfType(
				r.upgrades,
				ScalingInt(1),
				"shake",
				_recipes.value.map
			)
		_moneyState.value = _moneyState.value.copy(
			perShake = perShake
		)
	}

	fun toggleAdvancement(id: String) {
		_advancementState.value = _advancementState.value.copy(
			map = _advancementState.value.toggleAdvancement(id)
		)
	}
}
