package com.example.shaker.data

import androidx.annotation.BoolRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shaker.R

class Upgrade(
	@DrawableRes val imageResourceId: Int,
	@StringRes val name: Int,
	val id: Int,
	var amount: Int = 0,
	val price: Int = 1,
)

val upgrades = listOf(
	Upgrade(R.drawable.placeholder_0, R.string.upgrade_name, 0),
	Upgrade(R.drawable.placeholder_1, R.string.upgrade_name, 1),
	Upgrade(R.drawable.placeholder_2, R.string.upgrade_name, 2),
	Upgrade(R.drawable.placeholder_3, R.string.upgrade_name, 3),
	Upgrade(R.drawable.placeholder_4, R.string.upgrade_name, 4),
	Upgrade(R.drawable.placeholder_0, R.string.upgrade_name, 5),
	Upgrade(R.drawable.placeholder_1, R.string.upgrade_name, 6),
	Upgrade(R.drawable.placeholder_2, R.string.upgrade_name, 7),
	Upgrade(R.drawable.placeholder_3, R.string.upgrade_name, 8),
	Upgrade(R.drawable.placeholder_4, R.string.upgrade_name, 9),
	Upgrade(R.drawable.placeholder_0, R.string.upgrade_name, 10),
	Upgrade(R.drawable.placeholder_1, R.string.upgrade_name, 11),
	Upgrade(R.drawable.placeholder_2, R.string.upgrade_name, 12),
	Upgrade(R.drawable.placeholder_3, R.string.upgrade_name, 13),
	Upgrade(R.drawable.placeholder_4, R.string.upgrade_name, 14)
)