package com.example.shaker.ui.gameplayStates

import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allRecipes

data class RecipeState(
    val map: Map<Int, Long> = allRecipes.associate { it.id to 0L },
    val amountDiscovered: Int = 1
) {
    fun getRecipeAmount(recipe: Recipe): Long {
        return map[recipe.id]!!
    }

    //return an updated copy of the map that will trigger recomposition, to reassign to the map of a copy of this bigger object
    fun updatedMap(id: Int, amount: Long): Map<Int, Long> {
        return map.toMutableMap().apply {
            this[id] = amount
        }
    }

    fun incrementedMap(id: Int, amount: Long): Map<Int, Long> {
        return updatedMap(id, map[id]!! + amount)
    }

    fun incrementDiscovered(): Int {
        return amountDiscovered + 1
    }

    fun canBuy(recipe: Recipe, amount: Long, available: ScalingInt): Boolean {
        return recipe.getNextCost(this.getRecipeAmount(recipe), amount) <= available
    }

    fun canBuy(upgrade: Upgrade, amount: Long, available: ScalingInt): Boolean {
        return upgrade.getNextCost(amount) <= available
    }

    fun getNextCost(recipe: Recipe, amount: Long): ScalingInt {
        return recipe.getNextCost(this.getRecipeAmount(recipe), amount)
    }

    fun getNextCost(upgrade: Upgrade, amount: Long): ScalingInt {
        return upgrade.getNextCost(amount)
    }
}

