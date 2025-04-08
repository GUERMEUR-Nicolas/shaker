package com.example.shaker.ui.GameplayStates

import com.example.shaker.data.ScalingInt

data class MoneyState(
    val current : ScalingInt = ScalingInt(0),
	val previous : ScalingInt = ScalingInt(0),
    val perSecond : ScalingInt = ScalingInt(0),
    val perShake : ScalingInt = ScalingInt(1),
)

