package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
    var buildingB_ID: Int? = null,
    val cost: ScalingCost = ScalingCost(ScalingInt(1), 2.0),
    var level: Int = 0
){
	public fun doAllActionsOfType(original: ScalingInt, type: String, recipesInfo: Map<Int, Long> = mapOf<Int, Long>()): ScalingInt{
		var res: ScalingInt = original
		for(i in actions.indices){ // inner precedence implicit if actions[type] is sorted
			if(this.type[i] == type){
				val p = availableActions[type]?.get(actions[i])
				var amount: Long = 0
				if(type == "generate"){
					 /**/ if(i == 0){
						recipesInfo.forEach{(id, n) -> if(id != buildingB_ID) amount += n;}
					}else if(i == 3){
						amount = recipesInfo[buildingB_ID]!!
					}
				}
				res = p!!.first(res, p.second(i, amount))
			}
		}
		return res
	}

    public fun GetNextCost(amountBought: Long = 1): ScalingInt {
        return this.cost.GetNextCost(this.level.toLong(), amountBought)
    }


	private val availableActions: Map<String, Array<Pair<KFunction2<ScalingInt, Float, ScalingInt>, KFunction2<Int, Long, Float>>>> = mapOf(
		"generate" to arrayOf(
			Pair(ScalingInt::plus,  ::bARawCpsIncreaseByNumberOfOtherBuildings),
			Pair(ScalingInt::times, ::bARawEffectiveness),
			Pair(ScalingInt::times, ::timesX),
			Pair(ScalingInt::times, ::bACpsPortionIncreaseByNumberOfBB)
		),
		"cost" to arrayOf(
			Pair(ScalingInt::times, ::decrease),
			Pair(ScalingInt::times, ::increase)
		)
	)

	// Generate
    private fun bARawCpsIncreaseByNumberOfOtherBuildings(i: Int, amount: Long): Float{ // To use this, set buildingB_ID, as the building with this upgrade
        return this.x[i] * amount * this.level
    }
    private fun bARawEffectiveness(i: Int, amount: Long): Float{
        return this.x[i] * this.level
    }
	private fun timesX(i: Int, amount: Long): Float{
		return this.x[i] * this.level
	}
	private fun bACpsPortionIncreaseByNumberOfBB(i: Int, amount: Long): Float{
		return (1+this.x[i]) * amount * this.level
	}

	// Cost
	private fun decrease(i: Int, amount: Long): Float{
		return 1-this.x[i].pow(this.level)
	}
	private fun increase(i: Int, amount: Long): Float{
		return 1+this.x[i].pow(this.level)
	}
}

fun doAllUpgradesOfType(
	lst: List<Upgrade>,
	original: ScalingInt,
	type: String,
	recipesInfo: Map<Int, Long> = mapOf<Int, Long>()
): ScalingInt{
	if(lst.isEmpty())
		return original
	val toRun = mutableMapOf<Int, MutableList<Upgrade>>()
	var res: ScalingInt = original
	for(i in lst.indices){
		if(lst[i].level != 0 && type in lst[i].type) {
			if(toRun[i] == null)
				toRun[i] = mutableListOf<Upgrade>()
			toRun[i]!!.add(lst[i])
		}
	}
	if(toRun.isEmpty())
		return original
	toRun.forEach{(_, v) ->
		for(u in v)
			res = u.doAllActionsOfType(res, type, recipesInfo)
	}

	return res
}


val allUpgrades = arrayOf(
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
		R.string.upgrade_1,
		R.string.upgrade_description_1,
		R.drawable.upgrade_1,
        1,
		arrayOf(0, 1),
		arrayOf("generate", "cost"),
		arrayOf(0.1f, 0.05f)
	),
    Upgrade(
        R.string.upgrade_2,
        R.string.upgrade_description,
        R.drawable.upgrade_2,
        2,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    ),
    Upgrade(
        R.string.upgrade_3,
        R.string.upgrade_description,
        R.drawable.upgrade_3,
        3,
        arrayOf(2, 1),
        arrayOf("generate", "generate"),
        arrayOf(1.5f, 1.2f)
    ),
    Upgrade(
        R.string.upgrade_4,
        R.string.upgrade_description,
        R.drawable.upgrade_4,
        4,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    ),
    Upgrade(
        R.string.upgrade_5,
        R.string.upgrade_description,
        R.drawable.upgrade_5,
        5,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    ),
    Upgrade(
        R.string.upgrade_6,
        R.string.upgrade_description,
        R.drawable.upgrade_6,
        6,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    ),
    Upgrade(
        R.string.upgrade_7,
        R.string.upgrade_description,
        R.drawable.upgrade_7,
        7,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    ),
    Upgrade(
        R.string.upgrade_8,
        R.string.upgrade_description,
        R.drawable.upgrade_8,
        8,
        arrayOf(3),
        arrayOf("generate"),
        arrayOf(0.2f)
    )
)
