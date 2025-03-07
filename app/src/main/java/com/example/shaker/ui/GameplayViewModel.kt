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

    public fun Sell(recipe: Recipe, amount: Long) {
        ForceBuy(recipe, -amount)
    }



    public fun ForceBuy(recipe: Recipe, amount: Long) {
        val id = recipe.id
        _moneyState.value = _moneyState.value.copy(
            perSecond = _moneyState.value.perSecond + recipe.generating * amount,
            current = _moneyState.value.current - _recipes.value.GetNextCost(recipe,amount)
        )
        //We increment the recipe count after we effectively bought it, other ways, we'd pay one level ahead
        _recipes.value = _recipes.value.copy(
            map = _recipes.value.IncrementedMap(id, amount)
        )
    }

    public fun Increment(value: ScalingInt) {
        _moneyState.value = _moneyState.value.copy(
            current = _moneyState.value.current + value
        )
    }

    public fun Increment(numberOfCycle: Float) {
        Increment(_moneyState.value.perSecond * numberOfCycle)
    }

    public fun OnShaked() {
        Increment(_moneyState.value.perShake)
    }


}