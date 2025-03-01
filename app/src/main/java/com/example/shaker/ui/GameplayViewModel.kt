package com.example.shaker.ui

import androidx.lifecycle.ViewModel
import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.ui.GameplayStates.MoneyState
import com.example.shaker.ui.GameplayStates.RecipeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

class GameplayViewModel : ViewModel() {

    private val _moneyState = MutableStateFlow(MoneyState())
    val moneyState: StateFlow<MoneyState> = _moneyState.asStateFlow()
    private val _recipes = MutableStateFlow(RecipeState())
    val recipes: StateFlow<RecipeState> = _recipes.asStateFlow()
    fun GetRecipeAmount(recipe: Recipe) : Long{
        return _recipes.value.map[recipe.id]!!
    }
    private fun GetNextCost(recipe: Recipe): ScalingInt {
        return GetNextCost(recipe, 1)
    }

    private fun GetCost(recipe: Recipe, level: Long): ScalingInt {
        return recipe.basePrice * (level).toDouble().pow(recipe.scalingFactor)
    }

    public fun GetNextCost(recipe: Recipe, amount: Long): ScalingInt {
        var sum = ScalingInt(0)
        for (i in 0 until amount) {
            sum += GetCost(recipe, GetRecipeAmount(recipe) + i)
        }
        return sum
    }

    public fun Sell(recipe: Recipe, amount: Long) {
        ForceBuy(recipe, -amount)
    }

    fun canBuy(recipe: Recipe, amount: Long): Boolean {
        return _moneyState.value.current < GetNextCost(recipe, amount);
    }

    public fun ForceBuy(recipe: Recipe, amount: Long) {
        val id = recipe.id
        _recipes.value = _recipes.value.copy(
            map = _recipes.value.map.apply {
                this[id] = this[id]!! + amount
            }
        )
        _moneyState.value = _moneyState.value.copy(
            perSecond = _moneyState.value.perSecond + recipe.amount * amount,
            current = _moneyState.value.current - GetNextCost(recipe, amount)
        )
    }

    public fun Increment(value: ScalingInt) {
        _moneyState.value = _moneyState.value.copy(
            current = _moneyState.value.current + value
        )
    }

    public fun Increment(deltaSeconds: Float) {
        Increment(_moneyState.value.perSecond * deltaSeconds)
    }

    public fun OnShaked() {
        Increment(_moneyState.value.perShake)
    }


}