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
    UpgradeInfo(name=R.string.upgrade_0,image = R.drawable.upgrade_0, baseCost = 10, description = R.string.upgrade_description ),
    UpgradeInfo(name=R.string.upgrade_1,image = R.drawable.upgrade_1, baseCost = 25, description = R.string.upgrade_description ),
    UpgradeInfo(name=R.string.upgrade_2,image = R.drawable.upgrade_2, baseCost = 100, description = R.string.upgrade_description),
    UpgradeInfo(name=R.string.upgrade_3,image = R.drawable.upgrade_3, baseCost = 500, description = R.string.upgrade_description),
    UpgradeInfo(name=R.string.upgrade_4,image = R.drawable.upgrade_4, baseCost = 500, description = R.string.upgrade_description),
    UpgradeInfo(name=R.string.upgrade_5,image = R.drawable.upgrade_5, baseCost = 500, description = R.string.upgrade_description),
    UpgradeInfo(name=R.string.upgrade_6,image = R.drawable.upgrade_6, baseCost = 500, description = R.string.upgrade_description),
    UpgradeInfo(name=R.string.upgrade_7,image = R.drawable.upgrade_7, baseCost = 500, description = R.string.upgrade_description),
    UpgradeInfo(name=R.string.upgrade_8,image = R.drawable.upgrade_8, baseCost = 500, description = R.string.upgrade_description),
)
