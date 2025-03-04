package com.example.shaker.ui.GameplayStates

import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.allRecipes

data class RecipeState(
    val map : MutableMap<Int, Long> = recipes.associate({it.id to 0L}).toMutableMap()
)
