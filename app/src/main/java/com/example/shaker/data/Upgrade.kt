package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shaker.R

class Upgrade(
    @DrawableRes val imageResourceId: Int,
    @StringRes val name: Int
)

val upgrades = listOf(
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name),
    Upgrade(R.drawable.placeholder, R.string.upgrade_name)
)