package com.example.shaker.ui.GameplayStates

import com.example.shaker.data.recipes

data class RecipeState(
    val map : MutableMap<Int, Long> = recipes.associate({it.id to 0L}).toMutableMap()
)
