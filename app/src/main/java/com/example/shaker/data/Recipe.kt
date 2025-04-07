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
	var upgrades: MutableList<Upgrade> = mutableListOf<Upgrade>()
) {
    public fun GetNextCost(amountOwned: Long = 0, amountBought: Long = 1): ScalingInt {
        return doAllUpgradesOfType(this.upgrades, this.cost.GetNextCost(amountOwned, amountBought), "cost")
    }

    constructor(
        @DrawableRes imageResourceId: Int,
        generating10th: Double,
        name: Int,
        id: Int,
		upgrades: MutableList<Upgrade> = mutableListOf<Upgrade>()
	) : this(
        //When no cost is specified we use a cost based on the ID scaled by 11 on each increment
        cost = ScalingCost(ScalingInt(100.0* 11.0.pow(id - 1.0)),1.15),
        imageResourceId = imageResourceId,
        generating10th = generating10th,
        name = name,
        id = id,
		upgrades = upgrades
    )
    constructor(
        @DrawableRes imageResourceId: Int,
        generating10th: Double,
        name: Int,
        id: Int,
        cost : ScalingCost,
		upgrades: MutableList<Upgrade> = mutableListOf<Upgrade>()
	) : this(
        //When no cost is specified we use a cost based on the ID scaled by 11 on each increment
        cost = cost,
        imageResourceId = imageResourceId,
        generating = ScalingInt(generating10th*10),
        name = name,
        id = id,
		upgrades = upgrades
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
		upgrades = mutableListOf(
			allUpgrades[0],
			allUpgrades[1],
            allUpgrades[2],
            allUpgrades[3]
		)
    ),
    //From Second Recipe cost is based on the ID with a scaling factor
    Recipe(
        imageResourceId = R.drawable.cocktail_7,
        generating10th = 1.0,
        name = R.string.cocktail_7,
        id = 1,
        upgrades = mutableListOf(
            allUpgrades[0],
            allUpgrades[1],
            allUpgrades[2],
            allUpgrades[3]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_1,
        generating10th = 8.0,
        name = R.string.cocktail_1,
        id = 2,
        upgrades = mutableListOf(
            allUpgrades[0],
            allUpgrades[1],
            allUpgrades[2],
            allUpgrades[3]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_5,
        generating10th = 47.0,
        name = R.string.cocktail_5,
        id = 3,
        upgrades = mutableListOf(
            allUpgrades[0],
            allUpgrades[1],
            allUpgrades[2],
            allUpgrades[3]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_4,
        generating10th = 260.0,
        name = R.string.cocktail_4,
        id = 4,
        upgrades = mutableListOf(
            allUpgrades[3],
            allUpgrades[7],
            allUpgrades[8],
            allUpgrades[6]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_3,
        generating10th = 1400.0,
        name = R.string.cocktail_3,
        id = 5,
        upgrades = mutableListOf(
            allUpgrades[3],
            allUpgrades[1],
            allUpgrades[2],
            allUpgrades[4]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_2,
        generating10th = 7800.0,
        name = R.string.cocktail_2,
        id = 6,
        upgrades = mutableListOf(
            allUpgrades[7],
            allUpgrades[2],
            allUpgrades[6],
            allUpgrades[5]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.cocktail_6,
        generating10th = 44*1000.0,
        name = R.string.cocktail_6,
        id = 7,
        upgrades = mutableListOf(
            allUpgrades[1],
            allUpgrades[4],
            allUpgrades[3],
            allUpgrades[8]
        )
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_0,
        generating10th = 260*1000.0,
        name = R.string.recipe_name,
        id = 8
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_1,
        generating10th = 1.6*10.0.pow(6.0),
        name = R.string.recipe_name,
        id = 9
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_2,
        generating10th = 10*10.0.pow(6.0),
        name = R.string.recipe_name,
        id = 10
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_3,
        generating10th = 65*10.0.pow(6.0),
        name = R.string.recipe_name,
        id = 11
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_4,
        generating10th = 430*10.0.pow(6.0),
        name = R.string.recipe_name,
        id = 12
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_0,
        generating10th = 2.9*10.0.pow(9.0),
        name = R.string.recipe_name,
        id = 13
    ),
    Recipe(
        imageResourceId = R.drawable.placeholder_1,
        generating10th = 21*10.0.pow(9.0),
        name = R.string.recipe_name,
        id = 14
    )
)
