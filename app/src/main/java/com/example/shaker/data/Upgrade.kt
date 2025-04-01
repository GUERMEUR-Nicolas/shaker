package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shaker.R
import kotlin.reflect.KFunction2


class Upgrade(
	@StringRes val name: Int,
	@StringRes val description: Int,
	@DrawableRes val image: Int,
	private val actions: Array<Int>,
	val type: Array<String>,
	val tier: Tier,
	private val x: Array<Float>,
	val cost: ScalingInt = ScalingInt(1),
	val buildingB_ID: Int?
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

	public fun tierIfisTiered(): Int{
		return if(this.tier.level != 0) this.tier.level else 1
	}

	public fun getPrecedence(): Int{ // Lower is more important // TODO fix
		return actions.min() * 10
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
	private fun timesX(i: Int, amount: Long): Float{
		return this.x[i]
	}
	private fun bACpsPortionIncreaseByNumberOfBB(i: Int, amount: Long): Float{
		return (1+this.x[i]) * amount
	}
	private fun bARawCpsIncreaseByNumberOfOtherBuildings(i: Int, amount: Long): Float{ // To use this, set buildingB_ID, as the building with this upgrade
		return this.x[i] * amount
	}
	private fun bARawEffectiveness(i: Int, amount: Long): Float{
		return this.x[i]
	}

	// Cost
	private fun decrease(i: Int, amount: Long): Float{
		return 1-this.x[i]
	}
	private fun increase(i: Int, amount: Long): Float{
		return 1+this.x[i]
	}
}

fun doAllUpgradesOfType(
	lst: List<Pair<Upgrade, Boolean>>,
	original: ScalingInt,
	type: String,
	recipesInfo: Map<Int, Long> = mapOf<Int, Long>()
): ScalingInt{
	if(lst.isEmpty())
		return original
	val toRun = mutableMapOf<Int, MutableList<Upgrade>>()
	var res: ScalingInt = original
	for(i in lst.indices){
		if(lst[i].second && type in lst[i].first.type) {
			if(toRun[i] == null)
				toRun[i] = mutableListOf<Upgrade>()
			toRun[i]!!.add(lst[i].first)
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
		R.string.upgrade_name_0,
		R.string.upgrade_description_0,
		R.drawable.placeholder_0,
		arrayOf(2),
		arrayOf("generate"),
		allTiers[1],
		arrayOf(2.0f),
		buildingB_ID = null
	),
	Upgrade(
		R.string.upgrade_name_1,
		R.string.upgrade_description_1,
		R.drawable.placeholder_0,
		arrayOf(0, 1),
		arrayOf("generate", "cost"),
		allTiers[0],
		arrayOf(0.1f, 0.05f),
		buildingB_ID = null
	)
);
