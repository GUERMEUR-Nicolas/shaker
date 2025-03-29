package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import com.example.shaker.R

data class UpgradeInfo(
    @StringRes val name: Int,
    val level: Int = 0,
    val cost: ScalingCost,
    @StringRes val description: Int,
    @DrawableRes val image: Int){
    constructor(@StringRes name:Int, @StringRes description: Int, @DrawableRes image : Int, baseCost:Int): this(name, 0, ScalingCost(ScalingInt(baseCost)), description, image);
}
val allUpgrades = listOf(
    UpgradeInfo(name=R.string.upgrade_name, baseCost = 10, description = R.string.upgrade_description, image = R.drawable.placeholder_0),
    UpgradeInfo(name=R.string.upgrade_name, baseCost = 25, description = R.string.upgrade_description, image = R.drawable.placeholder_1),
    UpgradeInfo(name=R.string.upgrade_name, baseCost = 100, description = R.string.upgrade_description, image = R.drawable.placeholder_2),
    UpgradeInfo(name=R.string.upgrade_name, baseCost = 500, description = R.string.upgrade_description, image = R.drawable.placeholder_3),
)
