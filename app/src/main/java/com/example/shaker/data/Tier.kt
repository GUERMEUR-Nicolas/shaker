package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shaker.R

data class Tier( // Tier 0 == not a tiered upgrade
	@StringRes val name: Int?,
	val level: Int,
	@DrawableRes val effect: Int?
)

val allTiers = arrayOf(
	Tier(null, level = 0, null),
	Tier(R.string.tier_name, 1, R.drawable.placeholder_0),
)
