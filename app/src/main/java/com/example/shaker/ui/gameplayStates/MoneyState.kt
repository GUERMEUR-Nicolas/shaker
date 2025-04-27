package com.example.shaker.ui.gameplayStates

import com.example.shaker.data.ScalingInt

data class MoneyState(
	val current: ScalingInt = ScalingInt(0),
	val previous: ScalingInt = ScalingInt(0),
	val perSecond: ScalingInt = ScalingInt(0),
	val perShake: ScalingInt = ScalingInt(1),
) {
	constructor(initMoney: Int) : this(
		current = ScalingInt(initMoney),
	)

	constructor(current: ScalingInt, perSecond: ScalingInt, perShake: ScalingInt) : this(
		current = current,
		previous = current,
		perSecond = perSecond,
		perShake = perShake
	)
}

