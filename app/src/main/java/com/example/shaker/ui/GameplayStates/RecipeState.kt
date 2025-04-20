package com.example.shaker.ui.GameplayStates

import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allRecipes

data class RecipeState(
    val map: Map<Int, Long> = allRecipes.associate({ it.id to 0L }),
    val amountDiscovered: Int = 1
) {
    public fun GetRecipeAmount(recipe: Recipe): Long {
        return map[recipe.id]!!
    }

    //return an updated copy of the map that will trigger recompsition, to reassgin to the map of a copy of this bigger object
    public fun UpdatedMap(id: Int, amount: Long): Map<Int, Long> {
        return map.toMutableMap().apply {
            this[id] = amount
        }
    }

    public fun IncrementedMap(id: Int, amount: Long): Map<Int, Long> {
        return UpdatedMap(id, map[id]!! + amount)
    }

    fun incrementDiscovered(): Int{
        return amountDiscovered+1
    }

    fun canBuy(recipe: Recipe, amount: Long, available: ScalingInt): Boolean {
        return recipe.GetNextCost(this.GetRecipeAmount(recipe), amount) <= available;
    }

    fun canBuy(upgrade: Upgrade, amount: Long, available: ScalingInt): Boolean {
        return upgrade.GetNextCost(amount) <= available;
    }

    public fun GetNextCost(recipe: Recipe, amount: Long): ScalingInt{
        return recipe.GetNextCost(this.GetRecipeAmount(recipe), amount)
    }

    public fun GetNextCost(upgrade: Upgrade, amount: Long): ScalingInt{
        return upgrade.GetNextCost(amount)
    }
}

