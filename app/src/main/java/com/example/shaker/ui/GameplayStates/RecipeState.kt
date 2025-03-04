package com.example.shaker.ui.GameplayStates

import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.allRecipes

data class RecipeState(
    val map: Map<Int, Long> = allRecipes.associate({ it.id to 0L })
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

    public fun GetNextCost(recipe: Recipe, amount: Long = 1): ScalingInt {
        var sum = ScalingInt(0)
        for (i in 0 until amount) {
            sum += recipe.GetCost(this.GetRecipeAmount(recipe) + i)
        }
        return sum
    }

    fun canBuy(recipe: Recipe, amount: Long, available: ScalingInt): Boolean {
        return this.GetNextCost(recipe, amount) <= available;
    }
}

