package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shaker.R
import kotlin.math.pow
import kotlin.reflect.KFunction2


class Upgrade(
    @StringRes val name: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int,
    val id: Int,
    private val actions: Array<Int>,
    val type: Array<String>,
    private val x: Array<Float>,
    var recipeB_ID: Int? = null,
    private var cost: ScalingCost = ScalingCost(ScalingInt(1), 2.0),
    var level: Int = 0
) {
    constructor(upgrade: Upgrade) : this(
        name = upgrade.name,
        description = upgrade.description,
        image = upgrade.image,
        id = upgrade.id,
        actions = upgrade.actions.clone(),
        type = upgrade.type.clone(),
        x = upgrade.x.clone(),
        recipeB_ID = upgrade.recipeB_ID,
        cost = ScalingCost(upgrade.cost.basePrice, upgrade.cost.scalingFactor),
        level = 0
    )

    public fun SetRelative(index: Int) {
        recipeB_ID = index
    }

    public fun setBaseCost(value: ScalingInt) {
        this.cost = ScalingCost(value, this.cost.scalingFactor);
    }

    public fun doAllActionsOfType(
        original: ScalingInt,
        type: String,
        recipesInfo: Map<Int, Long> = mapOf<Int, Long>()
    ): ScalingInt {
        var res: ScalingInt = original
        for (i in actions.indices) { // inner precedence implicit if actions[type] is sorted
            if (this.type[i] == type) {
                val p = availableActions[type]?.get(actions[i])
                var amount: Long = 0
                if (p!!.second == ::bARawCpsIncreaseByNumberOfOtherBuildings) {
                    recipesInfo.forEach { (id, n) -> if (id != recipeB_ID) amount += n; }
                } else if (p.second == ::bACpsPortionIncreaseByNumberOfBB) {
                    amount = recipesInfo[recipeB_ID]!!
                }
                res = p.first(res, p.second(i, amount))
            }
        }
        return res
    }

    public fun GetNextCost(amountBought: Long = 1): ScalingInt {
        return this.cost.GetNextCost(this.level.toLong(), amountBought)
    }

    private val availableActions: Map<String, Array<Pair<KFunction2<ScalingInt, Float, ScalingInt>, KFunction2<Int, Long, Float>>>> =
        mapOf(
            "generate" to arrayOf(
                Pair(ScalingInt::plus,  ::bARawCpsIncreaseByNumberOfOtherBuildings),
                Pair(ScalingInt::times, ::bARawEffectiveness),
                Pair(ScalingInt::times, ::timesX),
                Pair(ScalingInt::times, ::bACpsPortionIncreaseByNumberOfBB)
            ),
            "shake" to arrayOf(
                Pair(ScalingInt::times, ::increase)
            ),
            "cost" to arrayOf(
                Pair(ScalingInt::times, ::decrease),
                Pair(ScalingInt::times, ::increase)
            )
        )

    // Generate
    private fun bARawCpsIncreaseByNumberOfOtherBuildings(
        i: Int,
        amount: Long
    ): Float { // To use this, set buildingB_ID, as the building with this upgrade
        return this.x[i] * amount * this.level
    }

    private fun bARawEffectiveness(i: Int, amount: Long): Float {
        return this.x[i] * this.level
    }

    private fun timesX(i: Int, amount: Long): Float {
        return this.x[i] * this.level
    }

    private fun bACpsPortionIncreaseByNumberOfBB(i: Int, amount: Long): Float {
        return (1 + this.x[i]) * amount * this.level
    }

    // Cost
    private fun decrease(i: Int, amount: Long): Float {
        return 1 - this.x[i].pow(this.level)
    }

    private fun increase(i: Int, amount: Long): Float {
        return 1 + this.x[i].pow(this.level)
    }

    @Composable
    fun getDynamicDescription(): String {
        return if (this.recipeB_ID != null)
            stringResource(this.description, stringResource(allRecipes[this.recipeB_ID!!].name))
        else
            stringResource(this.description, "")
    }
}

fun doAllUpgradesOfType(
    lst: List<Upgrade>,
    original: ScalingInt,
    type: String,
    recipesInfo: Map<Int, Long> = mapOf<Int, Long>()
): ScalingInt {
    if (lst.isEmpty())
        return original
    val toRun = mutableMapOf<Int, MutableList<Upgrade>>()
    var res: ScalingInt = original
    for (i in lst.indices) {
        if (lst[i].level != 0 && type in lst[i].type) {
            if (toRun[i] == null)
                toRun[i] = mutableListOf<Upgrade>()
            toRun[i]!!.add(lst[i])
        }
    }
    if (toRun.isEmpty())
        return original
    toRun.forEach { (_, v) ->
        for (u in v)
            res = u.doAllActionsOfType(res, type, recipesInfo)
    }

    return res
}

fun allUpgrades(index: Int, baseCost: ScalingInt): Upgrade {
    var upgrade = Upgrade(allUpgrades[index])
    upgrade.setBaseCost(baseCost);
    return upgrade;
}

private val allUpgrades = arrayOf(
    Upgrade(
        R.string.upgrade_0,
        R.string.upgrade_description_0,
        R.drawable.upgrade_0,
        0,
        arrayOf(2),
        arrayOf("generate"),
        arrayOf(2.0f)
    ),
    Upgrade(
        R.string.upgrade_3,
        R.string.upgrade_description_1,
        R.drawable.upgrade_3,
        1,
        arrayOf(0, 1),
        arrayOf("generate", "cost"),
        arrayOf(0.1f, 0.05f)
    ),
    Upgrade(
        R.string.upgrade_6,
        R.string.upgrade_description_2,
        R.drawable.upgrade_6,
        2,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    ),
    Upgrade(
        R.string.upgrade_8,
        R.string.upgrade_description_3,
        R.drawable.upgrade_8,
        3,
        arrayOf(0),
        arrayOf("shake"),
        arrayOf(1.1f)
    ),
    Upgrade(
        R.string.upgrade_2,
        R.string.upgrade_description_4,
        R.drawable.upgrade_2,
        3,
        arrayOf(2, 1),
        arrayOf("generate", "generate"),
        arrayOf(1.5f, 1.2f)
    ),
)
