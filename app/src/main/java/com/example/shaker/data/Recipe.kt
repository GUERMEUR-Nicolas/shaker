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

    public fun GetNextCost(amountOwned: Long = 0, amountBought: Long = 1): ScalingInt {
        var sum = ScalingInt(0)
        for (i in 0 until amountBought) {
            sum += this.GetCost(amountOwned + i)
        }
        return sum
    }
}

class Recipe(
    @DrawableRes val imageResourceId: Int,
    @StringRes val name: Int,
    val id: Int,
    var generating: ScalingInt = ScalingInt(0),
    var cost: ScalingCost,
    var upgrades: MutableList<Upgrade> = mutableListOf<Upgrade>(),
    var isDiscovered: Boolean = false
) {
    public fun GetNextCost(amountOwned: Long = 0, amountBought: Long = 1): ScalingInt {
        return doAllUpgradesOfType(
            this.upgrades,
            this.cost.GetNextCost(amountOwned, amountBought),
            "cost"
        )
    }

    constructor(
        @DrawableRes imageResourceId: Int,
        generating10th: Double,
        name: Int,
        id: Int,
        upgradesIndex: List<Int> = listOf(0, 1, 2, 3),
        relatedRecipes: List<Int?>,
        isDiscovered: Boolean = false
    ) : this(
        //When no cost is specified we use a cost based on the ID scaled by 11 on each increment
        imageResourceId = imageResourceId,
        generating10th = generating10th,
        name = name,
        id = id,
        cost = ScalingCost(ScalingInt(100.0 * 11.0.pow(id - 1.0)), 1.15),
        upgradesIndex = upgradesIndex,
        relatedRecipes = relatedRecipes,
        isDiscovered = isDiscovered
    )

    constructor(
        @DrawableRes imageResourceId: Int,
        generating10th: Double,
        name: Int,
        id: Int,
        cost: ScalingCost,
        upgradesIndex: List<Int>,
        relatedRecipes: List<Int?>,
        isDiscovered: Boolean = false
    ) : this(
        cost = cost,
        imageResourceId = imageResourceId,
        generating = ScalingInt(generating10th * 10),
        name = name,
        id = id,
        upgrades = upgradesIndex.map {
            //An upgrade base cost is 5 times bigger than buying the first recipe
            val upg = allUpgrades(it, cost.basePrice * 5)
            if (relatedRecipes[it] != null)
                upg.SetRelative(relatedRecipes[it]!!)
            upg
        }.toMutableList<Upgrade>(),
        isDiscovered = isDiscovered
    )
}

//TODO move this to the viewmodel
val allRecipes = listOf(
    Recipe(
        imageResourceId = R.drawable.cocktail_0,
        //Specific cost for the first recipe
        cost = ScalingCost(ScalingInt(15), 1.15),
        generating10th = 0.1,
        name = R.string.cocktail_0,
        id = 0,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null),
        isDiscovered = true
    ),
    //From Second Recipe cost is based on the ID with a scaling factor
    Recipe(
        imageResourceId = R.drawable.cocktail_7,
        generating10th = 1.0,
        name = R.string.cocktail_7,
        id = 1,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 0, null)

    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_1,
        generating10th = 8.0,
        name = R.string.cocktail_1,
        id = 2,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null)

    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_5,
        generating10th = 47.0,
        name = R.string.cocktail_5,
        id = 3,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null)

    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_4,
        generating10th = 260.0,
        name = R.string.cocktail_4,
        id = 4,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null)

    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_3,
        generating10th = 1400.0,
        name = R.string.cocktail_3,
        id = 5,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null)

    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_2,
        generating10th = 7800.0,
        name = R.string.cocktail_2,
        id = 6,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null)

    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_6,
        generating10th = 44 * 1000.0,
        name = R.string.cocktail_6,
        id = 7,
        upgradesIndex = listOf(0, 1, 2, 3),
        relatedRecipes = listOf(null, null, 1, null)
    )
)
