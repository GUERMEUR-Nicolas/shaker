package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shaker.R
import kotlin.math.pow

class ScalingCost(
    val basePrice: ScalingInt = ScalingInt(1),
    val scalingFactor: Double = 1.15
) {
    public fun GetCost(level: Long): ScalingInt {
        return this.basePrice * (this.scalingFactor).pow(level.toDouble())
    }
}

class Recipe(
    @DrawableRes val imageResourceId: Int,
    @StringRes val name: Int,
    val id: Int,
    var generating: ScalingInt = ScalingInt(0),
    var cost: ScalingCost = ScalingCost()
) {
    public fun GetCost(level: Long): ScalingInt {
        return cost.GetCost(level)
    }
}

//TODO move this to the viewmodel
val allRecipes = listOf(
    Recipe(
        imageResourceId = R.drawable.placeholder_0,
        cost = ScalingCost(ScalingInt(1), 1.15),

        generating = ScalingInt(1),
        name = R.string.recipe_name,
        id = 0
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_1,
        cost = ScalingCost(ScalingInt(5),1.15),
        name = R.string.recipe_name,
        id = 1
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_2,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(12),
        name = R.string.recipe_name,
        id = 2
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_3,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(60),
        name = R.string.recipe_name,
        id = 3
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_4,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(360),
        name = R.string.recipe_name,
        id = 4
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_0,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 5
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_1,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 6
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_2,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 7
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_3,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 8
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_4,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 9
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_0,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 10
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_1,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 11
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_2,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 12
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_3,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 13
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_4,
        cost = ScalingCost(ScalingInt(100), 1.15),
        generating = ScalingInt(1000),
        name = R.string.recipe_name,
        id = 14
    )
)